/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simulator.qam.hex;

import lattices.Hexagonal;
import simulator.VoronoiCodeAutoTranslation;

/**
 *
 * @author Robby McKilliam
 */
public class HexagonalCode 
        extends VoronoiCodeAutoTranslation {

    private final double[] temp = new double[2];

    public HexagonalCode(int M){
        super(new Hexagonal(), M); 
    }

    public double[] decode(double r, double i){
        temp[0] = r;
        temp[1] = i;
        return decode(temp);
    }

    public double[] encode(double r, double i){
        temp[0] = r;
        temp[1] = i;
        return encode(temp);
    }

}