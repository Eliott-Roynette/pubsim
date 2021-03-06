/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pubsim.lattices;

import Jama.Matrix;
import pubsim.lattices.decoder.ShortVectorSphereDecoded;
import pubsim.Util;
import pubsim.VectorFunctions;

/**
 * Class that represents a lattice with arbitrary
 * generator matrix.
 * @author Robby McKilliam
 */
public class GeneralLattice extends AbstractLattice {

    /** The generator matrix for the lattice */
    protected Matrix B;
    
    protected GeneralLattice(){
    }
    
    public GeneralLattice(Matrix B){
        this.B = B;
    }
    
    public GeneralLattice(double[][] B){
        this.B = new Matrix(B);
    }

    public Matrix getGeneratorMatrix() {
        return B;
    }

    public void setDimension(int n) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getDimension() {
        return B.rank();
    }

    public double coveringRadius() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void nearestPoint(double[] y) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double[] getLatticePoint() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double[] getIndex() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
