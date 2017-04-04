package laser;

/**
 * Created by Dr Theo Theodoridis.
 * Interface : Laser
 * Version   : v1.0
 * Date      : © Copyright 14/05/2015
 * User      : ttheod
 * email     : ttheod@gmail.com
 * Comments  : An interface for the laser class.
 **/

public interface Laser
{
    public boolean status();
    public void update();
    public double getRange(int index);
    public int getAngle(int index);
    public int getSize();
    public void printMap();
}
