/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pubsim.distributions.circular;

import pubsim.distributions.circular.CircularRandomVariable;
import pubsim.distributions.circular.WrappedUniform;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static pubsim.Util.dround6;

/**
 *
 * @author robertm
 */
public class WrappedUniformTest {

    public WrappedUniformTest() {
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
     * Test of pdf method, of class WrappedUniform.
     */
    @Test
    public void testPdf() {
        System.out.println("pdf");
        CircularRandomVariable instance = new WrappedUniform(0.0, 1.0/11.0);
        assertTrue(instance.pdf(0) < 1.0);
        System.out.println(instance.pdf(0));
        assertTrue(instance.pdf(-0.5) > 1.0);
        System.out.println(instance.pdf(-0.5));
        assertTrue(instance.pdf(0.5) > 1.0);
        System.out.println(instance.pdf(0.5));
    }

    /**
     * Test of getWrappedVariance method, of class WrappedUniform.
     */
    @Test
    public void testGetWrappedVariance() {
        System.out.println("getWrappedVariance");
        CircularRandomVariable instance = new WrappedUniform(0.0, 1.0/12.0);
        assertEquals(1.0/12.0, instance.unwrappedVariance(), 0.001);

        instance = new WrappedUniform(0.0, 1.0/11.0);
        double result = instance.unwrappedVariance();
        System.out.println(result);
        assertTrue(1.0/12.0 < result);
        assertTrue(1.0/11.0 > result);
    }

    
//
//    /**
//     * Test of setMean method, of class ProjectedNormalDistribution.
//     */
//    @Test
//    public void plotPdf() {
//        //System.out.println("plotPdf");
//        double mean = 0.0;
//        double var = 0.1;
//        WrappedUniform.Mod1 instance = new WrappedUniform.Mod1(mean, var);
//
//        double step = 0.01;
//        double intsum = 0.0;
//        int count = 0;
//        for (double x = -0.5; x <= 0.5; x += step) {
//            double pdf = instance.pdf(x);
//            System.out.println(dround6(x).toString().replace('E', 'e') + " " + dround6(pdf).toString().replace('E', 'e'));
//            intsum += pdf;
//            count++;
//        }
//
//        System.out.println(intsum*step);
//        System.out.println(instance.getWrappedVariance());
//
//    }

}