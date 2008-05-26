/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lattices;

import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import simulator.VectorFunctions;
import static org.junit.Assert.*;

/**
 *
 * @author Robby
 */
public class Phin2StarGreedyTest {

    public Phin2StarGreedyTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of nearestPoint method, of class Phin2StarGreedy.
     */
    @Test
    public void nearestPoint() {
        System.out.println("nearestPoint");
        int n = 12;
        Random rand = new Random();
        
        double[] y = new double[n];
        Phin2Star instance = new Phin2StarGreedy();
        Phin2Star tester = new Phin2StarGlued();
        
        for(int i = 0; i < 100; i++){
            for(int j=0; j<n; j++)
                y[j] = 10 * rand.nextGaussian();
            
            instance.nearestPoint(y);
            tester.nearestPoint(y);
            
            double dist = VectorFunctions.distance_between(instance.getLatticePoint(), tester.getLatticePoint());
            System.out.println("inst = " + VectorFunctions.distance_between(instance.getLatticePoint(), y));
            System.out.println("test = " + VectorFunctions.distance_between(tester.getLatticePoint(), y));
            System.out.println("inst = " + VectorFunctions.print(instance.getIndex()));
            System.out.println("test = " + VectorFunctions.print(tester.getIndex()));
            double[] s = VectorFunctions.subtract(instance.getIndex(), tester.getIndex());
            System.out.println("s = " + VectorFunctions.print(s));
            assertEquals(dist < 0.0001, true);
            
        }
    }

}