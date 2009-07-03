/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lattices.An;

import lattices.Anstar.AnstarVaughan;
import lattices.An.AnSorted;
import lattices.An.AnIntegerBabai;
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
 * @author harprobey
 */
public class AnIntegerBabaiTest {

    public AnIntegerBabaiTest() {
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
     * Test of nearestPoint method, of class AnIntegerBabai.
     */
    @Test
    public void testNearestPoint() {
               System.out.println("nearestPoint");

        int numTrials = 10000;
        int n = 20;
        Random rand = new Random();
        double[] y = new double[n];
        double[] v_instance = null;
        double[] v_tester = null;
        double[] x = new double[n];
        AnIntegerBabai instance = new AnIntegerBabai(n - 1);
        AnSorted tester = new AnSorted(n - 1);

        instance.setDimension(n - 1);
        tester.setDimension(n - 1);
        for(int i=0; i<numTrials; i++){
            for(int k = 0; k < n; k++){
                y[k] = rand.nextGaussian()*10.0;
            }
            instance.nearestPoint(y);
            tester.nearestPoint(y);
            v_instance = instance.getLatticePoint();
            v_tester = tester.getLatticePoint();
            AnstarVaughan.project(y,x);

            System.out.println(VectorFunctions.print(v_tester));
            System.out.println(VectorFunctions.print(v_instance));

            System.out.println(VectorFunctions.distance_between(v_instance, v_tester));
            assertEquals(VectorFunctions.distance_between(v_instance, v_tester) < 0.000001, true);
        }
    }

}