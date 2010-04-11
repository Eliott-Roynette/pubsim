/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lattices;

import Jama.Matrix;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import simulator.Util;
import simulator.VectorFunctions;
import static org.junit.Assert.*;

/**
 *
 * @author harprobey
 */
public class VnmTest {

    public VnmTest() {
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
     * Test of logVolume method, of class Vnm.
     */
    @Test
    public void testLogVolume() {
        System.out.println("logVolume");
        Vnm instance = new Vnm(5, 12);
        Matrix gen = instance.getGeneratorMatrix();
        Matrix gram = gen.transpose().times(gen);

        System.out.println(VectorFunctions.print(gram));

        assertEquals(Math.sqrt(gram.det()), instance.volume(), 0.0001);
    }


}