package laser; /**
 * Created by r.mouquet on 15/02/2017.
 */

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.phidgets.*;
import com.phidgets.event.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.TimeUnit;
import utils.Delay;


public class GUI {
    private JPanel motorController;
    private JPanel leftMotorPanel;
    private JPanel rightMotorPanel;
    private JLabel velocityLabel;
    private JLabel accelerationLabel;
    private JLabel brakingLabel;
    private JLabel backemfLabel;
    private JPanel tiltServoPanel;
    private JPanel panServoPanel;
    private JTextField leftMotorActualVelocityText;
    private JSlider leftMotorAccelerationSlider;
    private JTextField leftMotorAccelerationText;
    private JTextField leftMotorBrakingText;
    private JSlider leftMotorBrakingSlider;
    private JSlider tiltServoTargetPositionSlider;
    private JSlider slider2;
    private JSlider slider3;
    private JTextField textField3;
    private JTextField textField4;
    private JSlider rightMotorTargetVelocitySlider;
    private JSlider slider5;
    private JSlider slider6;
    private JTextField panServoTargetPositionText;
    private JSlider panServoTargetPositionSlider;
    private JTextField textField8;
    private JTextField textField9;
    private JSlider slider8;
    private JSlider slider9;
    private JLabel leftAttachedLabel;
    private JLabel rightAttachedLabel;
    private JLabel tiltAttachedLabel;
    private JLabel panAttachedLabel;
    private JSlider leftMotorTargetVelocitySlider;
    private JTextField leftMotorTargetVelocityText;
    private JButton resetButton;
    private JLabel leftMotorCurrentLabel;
    private JLabel rightMotorCurrentLabel;
    private JTextField rightMotorTargetVelocityText;
    private JTextField rightMotorActualVelocityText;
    private JTextField textField5;
    private JTextField textField6;
    private JLabel rightMotorActualVelocityLabel;
    private JLabel leftMotorEmfLabel;
    private JLabel rightMotorEmfLabel;
    private JTextField tiltServoTargetPositionText;
    private JTextField tiltServoActualPositionText;
    private JTextField panServoActualPositionText;
    private JLabel tiltServoActualPositionLabel;
    private JCheckBox tiltServoEngageCheckBox;
    private JCheckBox panServoEngageCheckBox;
    private JLabel tiltServocurrentLabel;
    private JLabel panServoCurrentLabel;

    private JFrame frame;

    double wheelCircumference = Math.PI*0.2;
    int leftEncoder=0, leftCounter=0, rightEncoder=0, rightCounter=0;
    boolean STOP = true;

    public MotorControlPhidget leftMotor = new MotorControlPhidget();
    public MotorControlPhidget rightMotor = new MotorControlPhidget();
    public AdvancedServoPhidget servo = new AdvancedServoPhidget();

    // CONSTRUCTOR


    public GUI() throws PhidgetException {

        //start();

        leftMotor.addAttachListener(attachHandler);
        leftMotor.addDetachListener(detachHandler);

        rightMotor.addAttachListener(attachHandler);
        rightMotor.addDetachListener(detachHandler);

        servo.addAttachListener(attachHandler);
        servo.addDetachListener(detachHandler);

        servo.addServoPositionChangeListener(positionHandler);

        //leftMotor.addMotorVelocityChangeListener(velocityHandler);
        //rightMotor.addMotorVelocityChangeListener(velocityHandler);

        leftMotor.addCurrentChangeListener(currentHandler);
        rightMotor.addCurrentChangeListener(currentHandler);

        leftMotor.addBackEMFUpdateListener(emfHandler);
        rightMotor.addBackEMFUpdateListener(emfHandler);

        leftMotor.addEncoderPositionUpdateListener(leftEncoderHandler);
        rightMotor.addEncoderPositionUpdateListener(rightEncoderHandler);



        leftMotorTargetVelocitySlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                leftMotorTargetVelocityText.setText(Integer.toString(leftMotorTargetVelocitySlider.getValue()));
                try {
                    leftMotor.setVelocity(0,leftMotorTargetVelocitySlider.getValue());
                } catch (PhidgetException e1) {
                    e1.printStackTrace();
                }
            }
        });

        rightMotorTargetVelocitySlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                rightMotorTargetVelocityText.setText(Integer.toString(rightMotorTargetVelocitySlider.getValue()));
                try {
                    rightMotor.setVelocity(0,rightMotorTargetVelocitySlider.getValue());
                } catch (PhidgetException e2) {
                    e2.printStackTrace();
                }
            }
        });

        leftMotorAccelerationSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                leftMotorAccelerationText.setText(Integer.toString(leftMotorAccelerationSlider.getValue()));
                try{
                    leftMotor.setAcceleration(0, leftMotorAccelerationSlider.getValue());
                } catch (PhidgetException e1) {
                    e1.printStackTrace();
                }
            }
        });
        leftMotorBrakingSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                leftMotorBrakingText.setText(Integer.toString(leftMotorBrakingSlider.getValue()));
                try {
                    leftMotor.setBraking(0, leftMotorBrakingSlider.getValue());
                } catch (PhidgetException e1) {
                    e1.printStackTrace();
                }
            }
        });

        panServoTargetPositionSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                panServoTargetPositionText.setText(Integer.toString(panServoTargetPositionSlider.getValue()));
                try {
                    servo.setPosition(0, panServoTargetPositionSlider.getValue());
                } catch (PhidgetException e1) {
                    e1.printStackTrace();
                }
            }
        });

        tiltServoTargetPositionSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                tiltServoTargetPositionText.setText(Integer.toString(tiltServoTargetPositionSlider.getValue()));
                try {
                    servo.setPosition(1, tiltServoTargetPositionSlider.getValue());
                } catch (PhidgetException e1) {
                    e1.printStackTrace();
                }
            }
        });

        panServoEngageCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean selected = panServoEngageCheckBox.isSelected();
                if(selected == true){
                    try {
                        servo.setEngaged(0,true);
                    } catch (PhidgetException e1) {
                        e1.printStackTrace();
                    }
                }
                else{
                    try {
                        servo.setEngaged(0,false);
                    } catch (PhidgetException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        tiltServoEngageCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean selected = tiltServoEngageCheckBox.isSelected();
                if(selected == true){
                    try {
                        servo.setEngaged(1,true);
                    } catch (PhidgetException e1) {
                        e1.printStackTrace();
                    }
                }
                else{
                    try {
                        servo.setEngaged(1,false);
                    } catch (PhidgetException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    leftMotor.setVelocity(0,0);
                    rightMotor.setVelocity(0,0);
                    servo.setEngaged(0,false);
                    servo.setEngaged(1,false);
                    STOP = false;
                } catch (PhidgetException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    AttachListener attachHandler = new AttachListener() {
        @Override
        public void attached(AttachEvent attachEvent) {

            int serialNumber = 0;
            String name = "";

            try {
                serialNumber = attachEvent.getSource().getSerialNumber();
                name = attachEvent.getSource().getDeviceName();
            } catch (PhidgetException e) {
                e.printStackTrace();
            }
            switch (serialNumber) {

                case 298883:
                    leftAttachedLabel.setText("YES :)");
                    leftAttachedLabel.setForeground(Color.green);
                    break;

                case 298924:
                    rightAttachedLabel.setText("YES :)");
                    rightAttachedLabel.setForeground(Color.green);
                    break;

                case 177097:
                    panAttachedLabel.setText("YES :)");
                    tiltAttachedLabel.setText("YES :)");
                    panAttachedLabel.setForeground(Color.green);
                    tiltAttachedLabel.setForeground(Color.green);
                    break;
            }
            System.out.println("Hello Device " + name + ", Serial Number: " + Integer.toString(serialNumber));
        }
    };

    DetachListener detachHandler = new DetachListener() {
        @Override
        public void detached(DetachEvent detachEvent) {

            int serialNumber =0;
            String name = "";

            try {
                serialNumber = detachEvent.getSource().getSerialNumber();
                name = detachEvent.getSource().getDeviceName();
            } catch (PhidgetException e) {
                e.printStackTrace();
            }
            switch (serialNumber){

                case 298883:
                    leftAttachedLabel.setText("NO :)");
                    leftAttachedLabel.setForeground(Color.red);
                    break;

                case 298924:
                    rightAttachedLabel.setText("NO :)");
                    rightAttachedLabel.setForeground(Color.red);
                    break;

                case 177097:
                    panAttachedLabel.setText("NO :)");
                    tiltAttachedLabel.setText("NO :)");
                    panAttachedLabel.setForeground(Color.red);
                    tiltAttachedLabel.setForeground(Color.red);
                    break;
            }
            System.out.println("Goodbye Device " + name + ", Serial Number: " + Integer.toString(serialNumber));
        }
    };

    CurrentChangeListener currentHandler = new CurrentChangeListener() {
        @Override
        public void currentChanged(CurrentChangeEvent currentChangeEvent) {

            int serialNumber = 0;

            try {
                serialNumber = currentChangeEvent.getSource().getSerialNumber();

            } catch (PhidgetException e) {
                e.printStackTrace();
            }
            switch (serialNumber){

                case 298883:
                    leftMotorCurrentLabel.setText(Double.toString(currentChangeEvent.getValue()));
                    break;

                case 298924:
                    rightMotorCurrentLabel.setText(Double.toString(currentChangeEvent.getValue()));
                    break;

        }
    }};

    BackEMFUpdateListener emfHandler = new BackEMFUpdateListener() {
        @Override
        public void backEMFUpdated(BackEMFUpdateEvent backEMFUpdateEvent) {
                int serialNumber = 0;

                try {
                    serialNumber = backEMFUpdateEvent.getSource().getSerialNumber();

                } catch (PhidgetException e) {
                    e.printStackTrace();
                }
                switch(serialNumber){

                    case 298883:
                        leftMotorEmfLabel.setText(Double.toString(backEMFUpdateEvent.getVoltage()));
                        break;

                    case 298924:
                        rightMotorEmfLabel.setText(Double.toString(backEMFUpdateEvent.getVoltage()));
                        break;
                }
            }
    };

    MotorVelocityChangeListener velocityHandler = new MotorVelocityChangeListener() {
            @Override
            public void motorVelocityChanged(MotorVelocityChangeEvent motorVelocityChangeEvent) {
                int serialNumber = 0;

                try {
                    serialNumber = motorVelocityChangeEvent.getSource().getSerialNumber();

                } catch (PhidgetException e) {
                    e.printStackTrace();
                }
                switch(serialNumber){

                    case 298883:
                        leftMotorActualVelocityText.setText(Double.toString(motorVelocityChangeEvent.getValue()));
                        break;

                    case 298924:
                        rightMotorActualVelocityText.setText(Double.toString(motorVelocityChangeEvent.getValue()));
                        break;
                }
            }
        };

    ServoPositionChangeListener positionHandler = new ServoPositionChangeListener() {
            @Override
            public void servoPositionChanged(ServoPositionChangeEvent servoPositionChangeEvent) {

                if(servoPositionChangeEvent.getIndex()==0){
                    try {
                        panServoActualPositionText.setText(Double.toString(servo.getPosition(0)));
                    } catch (PhidgetException e) {
                        e.printStackTrace();
                    }
                }
                else if(servoPositionChangeEvent.getIndex()==1){
                    try {
                        tiltServoActualPositionText.setText(Double.toString(servo.getPosition(1)));
                    } catch (PhidgetException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

    EncoderPositionUpdateListener leftEncoderHandler = new EncoderPositionUpdateListener() {
        @Override
        public void encoderPositionUpdated(EncoderPositionUpdateEvent encoderPositionUpdateEvent) {
            double vel=0;
            leftCounter ++;
            leftEncoder += encoderPositionUpdateEvent.getValue();

            if(leftCounter == 124){
                vel = (leftEncoder*wheelCircumference/245)*3.6; // vel = distance/time --- time = 125*8ms = 1 sec
                                                                // encoders : 245 pulses per rotation
                                                                // distance = (encoder ticks/245)*wheelCircumference
                leftEncoder = 0;
                leftCounter =0;
                leftMotorActualVelocityText.setText(Double.toString(vel));
            }
        }
    };

    EncoderPositionUpdateListener rightEncoderHandler = new EncoderPositionUpdateListener() {
        @Override
        public void encoderPositionUpdated(EncoderPositionUpdateEvent encoderPositionUpdateEvent) {
            double vel=0;
            rightCounter ++;
            rightEncoder += encoderPositionUpdateEvent.getValue();

            if(rightCounter == 124){
                vel = (rightEncoder*wheelCircumference/245)*3.6;

                rightEncoder = 0;
                rightCounter =0;
                rightMotorActualVelocityText.setText(Double.toString(vel));
            }

        }
    };



    // METHODS

    /**
     * Method     : GUI::start()
     * Purpose    : Methods used to diplay the Graphical User Interface GUI.
     * Parameters : None.
     * Returns    : Nothing.
     * Notes      : None.
     **/

    public void start() throws PhidgetException {

        // [+]Configure and show the frame:
        frame = new JFrame("Motor Controller for MEGABOT");
        frame.setContentPane(this.motorController);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


        leftMotor.open(298883);
        leftMotor.waitForAttachment();
        System.out.println("[+] LEFT motor attached...");

        rightMotor.open(298924);
        rightMotor.waitForAttachment();
        System.out.println("[+] RIGHT motor attached...");

        servo.open(177097);
        servo.waitForAttachment();
        System.out.println("[+] SERVO motor attached...");

        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                System.out.println("WINDOW CLOSED");
                try {
                    leftMotor.setVelocity(0,0);
                    rightMotor.setVelocity(0,0);
                    servo.setPosition(1,122);
                    servo.setPosition(0,64);
                    for(int i=0; i<8; i++) {
                        servo.setEngaged(i, false);
                    }
                } catch (PhidgetException e1) {
                    e1.printStackTrace();
                }
            }
        });

    }

    // SETTERS AND GETTERS

    public void setLeftMotorVelocity(double vel) throws PhidgetException {
        leftMotor.setVelocity(0,vel);
    }

    public void setRightMotorVelocity(double vel) throws PhidgetException {
        rightMotor.setVelocity(0,vel);
    }

    public void setServoPosition(int index, int value) throws PhidgetException {
        servo.setPosition(index, value);
    }

    public void setServoEngaged(int index, boolean state) throws PhidgetException {
        servo.setEngaged(index, state);
    }
}

