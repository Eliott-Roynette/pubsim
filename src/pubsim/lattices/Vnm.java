/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pubsim.lattices;

import Jama.Matrix;
import pubsim.lattices.decoder.ShortVectorSphereDecoded;
import pubsim.Util;
import pubsim.VectorFunctions;
import static pubsim.Util.binom;
import static pubsim.Util.log2Binom;
import static pubsim.Util.log2;
import static pubsim.Util.pow2;
import static pubsim.Util.erf;
import static pubsim.Util.erfc;
import static pubsim.Util.Q;
import static pubsim.Util.log2HyperSphereVolume;
import static pubsim.Util.hyperSphereVolume;

/**
 * Class for the lattice Vnm, ie the integer lattice that is that
 * duel of the polynomial phase estimation lattices Vnm*.  There is
 * no nearest point algorithms for these lattices (yet?).  Currently
 * only the volume method is implemented.
 * @author Robby McKilliam
 */
public class Vnm extends AbstractLattice{
    
    protected int a;
    protected int n;
    
    public Vnm(int a){
        this.a = a;
    }
    
    public Vnm(int a,int n){
        this.a = a;
        this.n = n;
    }

    public void setDimension(int n) {
        this.n = n;
    }

    /**
     * Uses nifty binomial formula to compute the volume.
     * @return
     */
    @Override
    public double volume() {
        return volume(a, n);
    }

    public static double volume(int a, int n){
        return Math.pow(2, logVolume(a, n));
    }

    /**
     * Uses nifty binomial formula to compute the log of the volume.
     * @return
     */
    @Override
    public double logVolume() {
        return logVolume(a, n);
    }

    /**
     * Uses nifty binomial formula to compute the log of the volume.
     * @return
     */
    public static double logVolume(int a, int n) {
        double vol = 0.0;
        for(int k = 0; k < a; k++){
            vol += ( Util.log2Binom(n+a+k, 2*k+1) - Util.log2Binom(2*k, k) );
        }
        return vol/2.0;
    }

    /**
     * This is Conway and Sloane's approximation for high SNR
     * probability of error in lattice coding.  Returrns log
     * base 10 of the probability of error.
     */
    @Override
    public double probCodingError(double S) {
        double deln = pow2((log2HyperSphereVolume(n) - logVolume())/n)*inradius();
        //System.out.println(deln);
        double erfcSdel = erfc( Math.sqrt(n*S/2.0) * deln );
        //System.out.println(erfcSdel);
        return 0.5*kissingNumber()*erfcSdel;
    }

    /**
     * Inradius is known for sufficiently large n and a less than 7.
     * Otherwise compute it by brute force.
     * The inradius is always greater that 2a.  This is due to a result
     * about the Tarry-Eschott problem.
     */
//    @Override
//    public double inradius() {
//        if(a > 6 || n < 27){
//            ShortestVector sv = new ShortestVector(this);
//            double norm = VectorFunctions.sum2(sv.getShortestVector());
//            return Math.sqrt(norm)/2.0;
//        }
//        else return Math.sqrt(2*a)/2.0;
//    }

    public Matrix getGeneratorMatrix() {
        
        double[] cv  = {1, -1};
        double[] bv = {1, -1};
        for(int i = 0; i < a-1; i++){
            bv = VectorFunctions.conv(cv, bv);
        }
        
        Matrix gen = new Matrix(n+a, n);
        for(int i = 0; i < n; i++){
            for(int j = 0; j < i; j++)
                gen.set(j,i,0.0);
            for(int j = i; j < i + bv.length; j++)
                gen.set(j,i,bv[j-i]);
            for(int j = i + bv.length; j < n + a; j++)
                gen.set(j,i,0.0);
        }
        
        return gen;
    }

    public double coveringRadius() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getDimension() {
        return n;
    }

//    public double unshapedProbCodingError(double S){
//        double nomgain = Math.pow(2.0, log2(2*a) - 2/n*logVolume());
//        double Ks = 2*kissingNumber()/n;
//        return Ks*Q(Math.sqrt(3*nomgain*S));
//    }

    /**
     * This is actually an upper bound on the kissing number for this
     * lattice.
     */
//    @Override
//    public long kissingNumber() {
//        return (long)Math.pow(2.0, log2Binom(n+a, 2*a) + log2Binom(2*a, a));
//        //return n*n;
//    }

}
