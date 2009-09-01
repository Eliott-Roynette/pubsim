/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simulator.qam.hex.ambiguity;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import lattices.Hexagonal;
import lattices.util.IntegerVectors;
import simulator.EinteinInteger;
import lattices.util.PointInSphere;
import simulator.qam.hex.HexagonalCode;

/**
 * Class to compute an upper bound on the number of codeword errors that occur
 * using Voronoi code based Hexagonal QAM.  The follows along the same lines
 * as Dan's paper, however in this case only an upper bound can be achieved.
 * @author Robby McKilliam
 */
public class HexAmbiguityCalculator {

    private final int r;
    private final double scale;
    private final Vector<M�biusAndNumDivisors> mset = new Vector<M�biusAndNumDivisors>();

    private final Iterable<EinteinInteger> code;
    private final Iterable<EinteinInteger> ring;

    /**
     *
     * @param r_hex Voronoi code scaling factor
     * @param transcale scaling due to the Voronoi code translation
     * that is required to make the codewords lie on the ring of
     * Einstein integers.
     */
    public HexAmbiguityCalculator(int r_hex, double transcale){
        this.r = r_hex;
        this.scale = transcale;

        //make an iterable for the scaled hex codewords
        code = new Iterable<EinteinInteger>() {
            HexagonalCode hex = new HexagonalCode(r);
            public Iterator<EinteinInteger> iterator() {
                return new Iterator<EinteinInteger>() {
                    IntegerVectors intvecs = new IntegerVectors(2, r);
                    public boolean hasNext() {
                        return intvecs.hasMoreElements();
                    }
                    public EinteinInteger next() {
                        intvecs.nextElement();
                        double[] i = intvecs.nextElementDouble();
                        double[] d = hex.encode(i);
                        return new EinteinInteger(scale*d[0], scale*d[1]);
                    }
                    public void remove() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                };
            }
        };

        //for( EinteinInteger c : code ) System.out.println(c);
        System.out.println("***********************************************");

        //get the magnitude of the maximum magnitude codeword.
        double magmax = Double.NEGATIVE_INFINITY;
        for( EinteinInteger p : code ){
            double mag = p.abs();
            if(mag > magmax) magmax = mag;
        }
        final double maxMag = magmax;

        //make an iterable for Eintien integers of sufficiently small magnitude
        ring = new Iterable<EinteinInteger>() {
            final double[] origin = {0.0,0.0};
            public Iterator<EinteinInteger> iterator() {
                return new Iterator<EinteinInteger>() {
                    PointInSphere points = new PointInSphere(new Hexagonal(), maxMag, origin);
                    public boolean hasNext() {
                        return points.hasMoreElements();
                    }
                    public EinteinInteger next() {
                        double[] d = points.nextElementDouble();
                        return new EinteinInteger(d[0], d[1]);
                    }
                    public void remove() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                };
            }
        };

        int ringsize = 0;
        for( EinteinInteger re : ring ) ringsize++;
        EinteinInteger[] ringa = new EinteinInteger[ringsize];
        int count = 0;
        for( EinteinInteger re : ring ){ 
            ringa[count] = re;
            count++;
        }
        Arrays.sort(ringa);

        //for( EinteinInteger re : ring ) System.out.println(re);
        System.out.println("***********************************************");

//        int ringsize = 0;
//        for( EinteinInteger re : ring ) ringsize++;
//        int codesize = 0;
//        for( EinteinInteger c : code ) codesize++;
//
//        System.out.println(codesize);
//        System.out.println(ringsize);
//
//        setup mset with enough memory.
//        mset = new Vector<M�biusAndNumDivisors>(ringsize*codesize);
        

        //compute the M�bius and divisors array
        for( int re = 0; re < ringa.length; re++ ){
            if(!ringa[re].isUnit()){
                int numdivs = 0;
                for( EinteinInteger c : code ){
                    if( EinteinInteger.divides(ringa[re], c)) numdivs++;
                }
                //System.out.println(new M�biusAndNumDivisors(ringa[re].factorise(ringa), numdivs));
                mset.add(new M�biusAndNumDivisors(ringa[re].factorise(ringa), numdivs));
            }
        }

        System.out.println(mset.size());
        //for( M�biusAndNumDivisors m : mset ) System.out.println(m);
        System.out.println("***********************************************");

    }


    public long upperBoundAmbiguousCodewords(int N){
        long ambs = 0;
        for( M�biusAndNumDivisors m : mset ){
            ambs -= (long)(m.m�bius * Math.pow(m.numDivisors, N));
        }
        return ambs;
    }

    public double upperBoundBLER(int N){
        return upperBoundAmbiguousCodewords(N)/Math.pow(r*r, N);
    }


    /**
     * Class to hold the M�bius number and the number of codeword divisors
     * for a particular Gaussian integer.
     */
    public class M�biusAndNumDivisors{
        public final int m�bius;
        public final int numDivisors;
        public M�biusAndNumDivisors(Collection<EinteinInteger> factors, int d){
            m�bius = m�biusFunction(factors);
            numDivisors = d;
        }

        /** return a string representation of the invoking Complex object */
        @Override
        public String toString() {
            return m�bius + ", " + numDivisors;
        }

    }

    //Compute the M�bius function from a collection of factors.
    public static int m�biusFunction(Collection<EinteinInteger> factors){

        //if there is only one element and it is a unit then return 1
        if(factors.size() == 1){
            if( factors.iterator().next().isUnit() ) return 1;
        }

        //copy this to an array, java's iterators are a bit crap.
        EinteinInteger[] farray =
                factors.toArray(new EinteinInteger[factors.size()]);

        //System.out.println(factors);

        for(int f1 = 0; f1 < farray.length; f1++){
            for(int f2 = f1+1; f2 < farray.length; f2++){
                if(EinteinInteger.equivalentIdeal(farray[f1], farray[f2]))
                    return 0;
            }
        }

        //otherwise
        return (int)Math.pow(-1, factors.size());
    }

}
