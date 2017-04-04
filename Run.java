package laser;

/**
 * Created by Dr Theo Theodoridis.
 * Class    : Run
 * Version  : v1.0
 * Date     : © Copyright 14/05/2015
 * User     : ttheod
 * email    : ttheod@gmail.com
 * Comments : The class creates an interface for accessing the RPLidar laser scanner sensor.
 **/

public class Run
{
    public static void main(String args[]) throws Exception
    {
        Laser laser = new Laser180("COM14");
        GUI gui = new GUI();
        gui.start();
        Behaviour behaviour = new Behaviour(gui, laser);
        //laser.printMap();

        while(gui.STOP == true) {

            behaviour.avoid(60);
        }

    }
}
