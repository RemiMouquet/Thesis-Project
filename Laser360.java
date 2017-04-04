package laser;

import java.awt.*;
import java.util.ArrayList;
import org.jfree.ui.RefineryUtilities;
import utils.ScatterPlotter;
import utils.Delay;

/**
 * Created by Dr Theo Theodoridis.
 * Class    : Laser360
 * Version  : v1.0
 * Date     : © Copyright 14/05/2015
 * User     : ttheod
 * email    : ttheod@gmail.com
 * Comments : The class creates an interface for accessing 360 degrees from the RPLidar laser scanner sensor.
 **/

public class Laser360 implements Laser
{
    private RpLidarScan scan;
    private RpLidarHighLevelDriver driver;

    private ArrayList<Double> mmList;
    private ArrayList<Integer> thList;
    private double readings[] = new double[RpLidarScan.N];

    private int th[];
    private double mm[];
    private boolean statusFlag = true;

    public Laser360(String com)
    {
        driver = new RpLidarHighLevelDriver();
        if(!driver.initialize(com, 0)) statusFlag = false;
        else
        {
            statusFlag = true;
            update();
        }
    }

    public boolean status()
    {
        return(statusFlag);
    }

    public void update()
    {
        mmList = new ArrayList<Double>();
        thList = new ArrayList<Integer>();

        scan = new RpLidarScan();
        if(!driver.blockCollectScan(scan, 10000))
        {
            System.out.println("Couldn't collect a complete scan");
        }
        else
        {
            scan.convertMilliMeters(readings);
            for(int i=0 ; i<RpLidarScan.N ; i++)
            {
                if(scan.distance[i] != 0)
                {
                    thList.add(i / 64);
                    mmList.add(readings[i]);
                }
            }
        }

        int j=0;
        th = new int[mmList.size()];
        mm = new double[mmList.size()];

        // [+]1st quartile: [0, 90]
        for(int i=(int)((double)mmList.size()*0.75) ; i<mmList.size() ; i++)
        {
            th[j] = thList.get(i);
            mm[j++] = mmList.get(i);
        }
        // [+]2nd quartile: [90, 180]
        for(int i=0 ; i<(int)((double)mmList.size()*0.25) ; i++)
        {
            th[j] = thList.get(i);
            mm[j++] = mmList.get(i);
        }
        // [+]3rd quartile: [180, 270]
        for(int i=(int)((double)mmList.size()*0.25) ; i<(int)((double)mmList.size()*0.5) ; i++)
        {
            th[j] = thList.get(i);
            mm[j++] = mmList.get(i);
        }
        // [+]4th quartile: [270, 360]
        for(int i=(int)((double)mmList.size()*0.5) ; i<(int)((double)mmList.size()*0.75)-1 ; i++)
        {
            th[j] = thList.get(i);
            mm[j++] = mmList.get(i);
        }
    }

    public double getRange(int index)
    {
        return(mm[index]);
    }

    public int getAngle(int index)
    {
        return(th[index]);
    }

    public int getSize()
    {
        return(mmList.size());
    }

    public void printMap()
    {
        ScatterPlotter scatterPlotter = new ScatterPlotter("Laser Scanner", "Scatter Plot", "X", "Y", "Data", Color.BLUE, false);
        scatterPlotter.pack();
        RefineryUtilities.centerFrameOnScreen(scatterPlotter);
        scatterPlotter.setVisible(true);

        while(true)
        {
            update();
            for(int i=0 ; i<getSize() ; i++)
            {
                double x = getRange(i) * Math.cos(Math.toRadians(getAngle(i)));
                double y = getRange(i) * Math.sin(Math.toRadians(getAngle(i)));
                double d = Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0));
                if(d <= 6000.0)
                scatterPlotter.series.add(x, y);
                //System.out.println("[" + getAngle(i) + "] = " + getRange(i));
            }
            Delay.ms(100);
            scatterPlotter.series.clear();
        }
    }

    // OBSTACLE AVOIDANCE FUNCTION

    public void avoid(){

        while(true){

            update();

            for(int i=0 ; i<getSize() ; i++)
            {
                double x = getRange(i) * Math.cos(Math.toRadians(getAngle(i)));
                double y = getRange(i) * Math.sin(Math.toRadians(getAngle(i)));
                double d = Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0));

                if(d <= 600.0)
                    System.out.println("++ OBSTACLE DETECTED ++");

                System.out.println("[" + getAngle(i) + "] = " + getRange(i));
            }
            Delay.ms(100);
        }
    }

}
