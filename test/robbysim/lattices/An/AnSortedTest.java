/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package robbysim.lattices.An;

import robbysim.lattices.An.AnSorted;
import junit.framework.TestCase;
import robbysim.distributions.processes.IIDNoise;
import robbysim.distributions.UniformNoise;
import robbysim.VectorFunctions;

/**
 *
 * @author Robby
 */
public class AnSortedTest extends TestCase {
    
    public AnSortedTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of nearestPoint method, of class AnSorted.
     */
    public void testNearestPoint() {
        System.out.println("nearestPoint");
        
        //this test uses the fact that I know that
        //any value in the unit cube should have 
        //the origin as the closest point in An
        
        int n = 10;
        int iters = 100;
        
        UniformNoise noise = new UniformNoise(0,1.0,0);
        IIDNoise siggen = new IIDNoise();
        siggen.setNoiseGenerator(noise);
        siggen.setLength(n);
        
        AnSorted instance = new AnSorted(n-1);
        //instance.setDimension(n - 1);
        
        for(int i = 0; i < iters; i++){
            double[] y = siggen.generateReceivedSignal();
            instance.nearestPoint(y);
            assertEquals(0.0, VectorFunctions.sum(instance.getLatticePoint()));
        }
        
    }


}