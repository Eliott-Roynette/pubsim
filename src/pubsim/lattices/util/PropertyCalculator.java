/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pubsim.lattices.util;

import java.io.Serializable;
import pubsim.lattices.LatticeAndNearestPointAlgorithm;

/**
 * Abstract class for brute forcing approximating properties of lattices.
 * Covering radius, moments etc.
 * @author Robby McKilliam
 */
abstract class PropertyCalculator implements Serializable{

    protected final LatticeAndNearestPointAlgorithm L;
    protected int numsamples;

    protected PropertyCalculator(){L = null;}

    public PropertyCalculator(LatticeAndNearestPointAlgorithm L){
        this.L = L;
    }

    /**
     * Spreads points evenly in Voronoi region to compute moments.
     * @param samples per dimension
     */
    public void evenlySampled(int samples){
        numsamples = samples;
        PointEnumerator points = new SampledInVoronoi(L, samples);
        while(points.hasMoreElements()){
            calculateProperty(points.nextElementDouble());

        }
    }

    /**
     * Print percentage complete to System.out while running.  Value of print boolean
     * does not matter.
     */
    public void evenlySampledPrintPercentage(int samples){
        numsamples = samples;
        PointEnumerator points = new SampledInVoronoi(L, samples);
        int oldper = 0;
        while(points.hasMoreElements()){
            calculateProperty(points.nextElementDouble());
            int p = (int)points.percentageComplete();
            if(p == oldper){
                System.out.println(p + "%");
                System.out.flush();
                oldper = p + 5;
            }
        }
    }

    /**
     * Runs with points generated in uniformly (psuedoranomly) in the Voronoi region.
     * @param samples number of points generated.
     */
    public void uniformlyDistributed(int samples){
        numsamples = samples;
        final PointEnumerator points = new UniformInVornoi(L, Integer.MAX_VALUE);
        double oldG = 0;
        int count = 0;
        for(int n = 0; n < samples; n++)
            calculateProperty(points.nextElementDouble());
    }

    /**
     * Runs with points generated in uniformly (psuedoranomly) in the Voronoi region.
     * @param samples number of points generated.
     */
    public void uniformlyDistributedPrintPercentage(int samples){
        numsamples = samples;
        final PointEnumerator points = new UniformInVornoi(L, Integer.MAX_VALUE);
        double oldG = 0;
        int count = 0;
        int lastpercent = 0;
        for(int n = 0; n < samples; n++){
            calculateProperty(points.nextElementDouble());

            int percentComplete = (int)(100*(((double)n)/samples));
            if( (percentComplete%10) == 0 && percentComplete != lastpercent){
                System.out.print(percentComplete + "% ");
                lastpercent = percentComplete;
            }

        }
    }

    abstract protected void calculateProperty(double[] p);


}
