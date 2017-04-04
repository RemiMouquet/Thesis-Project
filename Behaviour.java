
package laser;

import com.phidgets.PhidgetException;
import utils.Delay;
import java.util.concurrent.TimeUnit;

/**
 * Created by r.mouquet on 23/03/2017.
 */

public class Behaviour {

    public GUI gui;
    public Laser laser;
    int standCounter=0;

    public Behaviour(GUI gui, Laser laser){

       this.gui = gui;
       this.laser = laser;

    }

    public double getDistance(int i){

        double x = laser.getRange(i) * Math.cos(Math.toRadians(laser.getAngle(i)));
        double y = laser.getRange(i) * Math.sin(Math.toRadians(laser.getAngle(i)));
        double d = Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0));
        return d;
    }

    public void turnOnspot(double vel) throws PhidgetException {

        gui.setLeftMotorVelocity(vel);
        gui.setRightMotorVelocity(vel);
    }

    public void moveBackward(double vel) throws PhidgetException {

        gui.setLeftMotorVelocity(-vel);
        gui.setRightMotorVelocity(vel);
    }

    public void avoid(int vel) throws PhidgetException {

        int leftStopCounter=0, rightStopCounter=0, closeRightCounter=0, closeLeftCounter=0;

        laser.update();

        System.out.println("SIZE : " + laser.getSize());

        for(int i=0; i< laser.getSize(); i++){

            // LEFT STOP OBSTACLE
            if(getDistance(i) < 600 && laser.getAngle(i) > 90){
                leftStopCounter += 1;
            }

            // RIGHT STOP OBSTACLE
            if(getDistance(i) < 600 && laser.getAngle(i) < 90){
                rightStopCounter += 1;
            }

            // CLOSE LEFT OBSTACLE
            else if(getDistance(i) >= 600 && getDistance(i) <= 1000 && laser.getAngle(i) > 90){
                closeLeftCounter += 1;
            }

            // CLOSE RIGHT OBSTACLE
            else if(getDistance(i) >= 600 && getDistance(i) <= 1000 && laser.getAngle(i) < 90){
                closeRightCounter += 1;
            }
        }

        System.out.println("LEFT STOP : " + leftStopCounter + "RIGHT STOP : " + rightStopCounter + " CLOSE LEFT : " + closeLeftCounter +
        "CLOSE RIGHT : " + closeRightCounter);


        // WHEN STOPPED FOR 5 CYCLE, GO BACKWARD AND TURN ON SPOT
        if(leftStopCounter > 5 || rightStopCounter > 5){
            System.out.println("STOPPING");
            standCounter += 1;
            gui.setLeftMotorVelocity(0);
            gui.setRightMotorVelocity(0);

            if(standCounter == 5 && leftStopCounter > rightStopCounter){
                standCounter = 0;
                moveBackward(vel);
                Delay.ms(1000);
                turnOnspot(vel);
            }

            else if(standCounter == 5 && rightStopCounter > leftStopCounter){
                standCounter = 0;
                moveBackward(vel);
                Delay.ms(1000);
                turnOnspot(-vel);
            }

        }

        else if(closeRightCounter > closeLeftCounter+3){
            System.out.println("TURNING LEFT");
            gui.setLeftMotorVelocity(vel/3);
            gui.setRightMotorVelocity(-vel);
            gui.setServoPosition(0,84);
            gui.setServoPosition(1,112);
            gui.setServoEngaged(0, true);
            gui.setServoEngaged(1, true);
        }

        else if(closeLeftCounter > closeRightCounter+3){
            System.out.println("TURNING RIGHT");
            gui.setLeftMotorVelocity(vel);
            gui.setRightMotorVelocity(-vel/3);
            gui.setServoPosition(0,44);
            gui.setServoPosition(1,112);
            gui.setServoEngaged(0, true);
            gui.setServoEngaged(1, true);
        }

        else if(leftStopCounter < 5 && rightStopCounter < 5 && closeRightCounter < 15 && closeLeftCounter < 15){
            System.out.println("GOING FORWARD");
            gui.setLeftMotorVelocity(vel*1.125);
            gui.setRightMotorVelocity(-vel);
            gui.setServoPosition(0,64);
            gui.setServoPosition(1,122);
            gui.setServoEngaged(0, true);
            gui.setServoEngaged(1, true);
        }

        Delay.ms(250);

    }

}
