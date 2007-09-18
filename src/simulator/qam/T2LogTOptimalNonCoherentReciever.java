/*
 * T2LogTOptimalNonCoherentReciever.java
 *
 * Created on 17 September 2007, 14:10
 */

package simulator.qam;

import java.util.TreeMap;
import simulator.VectorFunctions;

/**
 * The faster O(T^2 log(T)) GLRT-optimal non-coherent QAM
 * receiver. 
 * @author Robby
 */
public class T2LogTOptimalNonCoherentReciever implements  QAMReceiver {
    
    protected int M;
    protected int T;
    
    protected double[] y1, y2;
    protected double[] v, vbest;
    protected double[] c;
    protected double[] d;
    protected double[] dreal;
    protected double[] dimag;
    
    protected TreeMap map;
    
    /** Set the size of the QAM array */
    public void setQAMSize(int M){
        this.M = M;
    }
    
    /** 
     * Set number of QAM signals to use for
     * estimating the channel
     */
    public void setT(int T){
        this.T = T;
        
        y1 = new double[2*T];
        y2 = new double[2*T];
        v = new double[2*T];
        vbest = new double[2*T];
        c = new double[2*T];
        d = new double[2*T];
        
        dreal = new double[T];
        dimag = new double[T];
        
        map = new TreeMap();
    }
    
    /**Decode the QAM signal*/
    public void decode(double[] rreal, double[] rimag){
        if( rreal.length != T )
            setT(rreal.length);
        
        //Dan's underline operator
        for(int i = 0; i < T; i++){
            y1[2*i] = rreal[i];
            y1[2*i+1] = rimag[i];
            y2[2*i] = -rimag[i];
            y2[2*i+1] = rreal[i];
        }
        
        double Lbest = Double.POSITIVE_INFINITY;
        //for each type of line
        for(int i = 0; i < 2*T; i++){
            
            //for the parallel lines of this type
            for(int k = -M+2; k <= M-2; k+=2){
                
                //calculate parameters for the 
                //line we are searching.  d can be
                //calculated outside of k loop but
                //it's neater to have it here
                for(int j = 0; j < 2*T; j++){
                    c[j] = k*y1[j]/y1[i];
                    d[j] = -y2[i]*y1[j] - y2[j];
                }
                
                //calculate thestart point
                //for the line search
                double bmin = Double.NEGATIVE_INFINITY;
                int minT = 0;
                for(int j = 0; j < 2*T; j++){
                    if(j!=i && d[j] != 0.0){
                        double bpos = (M - 2 - c[j])/d[j];
                        double bneg = (-M + 2 - c[j])/d[j];
                        double thismin = Math.min(bpos, bneg);
                        if(bmin < thismin){
                            minT = j;
                            bmin = thismin;
                        }
                    }
                }
                
                //setup start point
                map.clear();
                for(int j = 0; j < 2*T; j++)
                        v[j] = 2*Math.round((bmin*d[j]+c[j]+1)/2)-1;
                v[i] = k + 1;
                v[minT] = -Math.signum(d[minT])*(M - 1);
                
                //setup map
                map.clear();
                for(int j = 0; j < 2*T; j++){
                    if(i!=j && d[j] != 0.0)
                        map.put(new Double((v[j]+Math.signum(d[j])-c[j])/d[j]),
                                new Integer(j));
                }
                
                //setup dot product variables for updating 
                //likelihood in constant time.
                double y1tv = 0.0, y2tv = 0.0, y1ty2 = 0.0, vtv = 0.0,
                        y1tvn = 0.0, y2tvn = 0.0, vtvn = 0.0,
                        y1ty1 = 0.0, y2ty2 = 0.0;
                for(int j = 0; j < 2*T; j++){
                    y1tv += y1[j]*v[j];
                    y2tv += y2[j]*v[j];
                    y1ty2 += y1[j]*y2[j];
                    vtv += v[j]*v[j];
                    y1ty1 += y1[j]*y1[j];
                    y2ty2 += y2[j]*y2[j];
                }
                y1tvn = y1tv - 2*y1[i];
                y2tvn = y2tv - 2*y2[i];
                vtvn = vtv - 4*v[i] + 4;
                
                //run the search loop
                int n = 0;
                //int count = 0;
                while( (v[n] >= -M + 1) && (v[n] <= M - 1)){
                    
                    //test the likelihood of the codeword on each
                    //side of the line, runs in constant time.
                    double L = vtv - y1tv*y1tv/y1ty1 - y2tv*y2tv/y2ty2
                            + y1tv*y2tv*y1ty2/(y1ty1*y2ty2);
                    if(L < Lbest){
                        Lbest = L;
                        for(int j = 0; j < 2*T; j++)
                            vbest[j] = v[j];
                        System.out.println("*****");
                    }
                    double Ln = vtvn - y1tvn*y1tvn/y1ty1 - y2tvn*y2tvn/y2ty2
                        + y1tvn*y2tvn*y1ty2/(y1ty1*y2ty2);
                    if(Ln < Lbest){
                        Lbest = Ln;
                        for(int j = 0; j < 2*T; j++)
                            vbest[j] = v[j];
                        vbest[i] -= 2;
                        System.out.println("*****");
                    }
                    
                    System.out.println("L = " + L + ", Ln = " + Ln);
                    System.out.println("v = " + VectorFunctions.print(v));
                    
                    Double key = ((Double) map.firstKey());
                    n = ((Integer)map.get(key)).intValue();
                    double s = Math.signum(d[n]);
                    
                    //update the dot products
                    y1tv += 2*s*y1[i];
                    y2tv += 2*s*y2[i];
                    vtv += 4*s*v[i] + 4;
                    y1tvn += 2*s*y1[i];
                    y2tvn += 2*s*y2[i];
                    vtvn += 4*s*v[i] + 4;
                    
                    v[n] += 2*s;
                    map.remove(key);
                    map.put(new Double((v[n]+s-c[n])/d[n]), new Integer(n));

                }
                
                System.out.println("end " + i + ", " + k);
                System.out.println("");
                
            }
            
        }
        
        //Write the best codeword into real and
        //imaginary vectors
        for(int i = 0; i < T; i++){
            dreal[i] = vbest[2*i];
            dimag[i] = vbest[2*i + 1];
        }
        
    }
    
    /** 
     * Nearest neighbour algorithm for this form of QAM.
     * See Dan's paper. x input, y output.
     * pre: x.length == y.length.
     */
    public void NN(double[] x, double[] y){
        for(int i = 0; i < x.length; i++)
            y[i] = 2*Math.round((x[i]+1)/2) - 1;
    }
    
    /** 
     * Get the real part of the decoded QAM signal.
     * Call decode first.
     */
    public double[] getReal(){
        return dreal;
    }
    
    /** 
     * Get the imaginary part of the decoded QAM signal.
     * Call decode first.
     */
    public double[] getImag(){
        return dimag;
    }
    
    /**
     * Tests two QAM symbols for equality up to the
     * ambiguity of an pi/2 rotation in phase.
     */
    public static boolean ambiguityEqual(double[] xreal, double[] ximag, 
                                            double[] yreal, double[] yimag){
        boolean ret = true;
        for(int i = 0; i < xreal.length; i++)
            ret = ret && (xreal[i] == yreal[i])&&(ximag[i] == yimag[i]);
        if(ret == true) return true;
        
        ret = true;
        for(int i = 0; i < xreal.length; i++)
            ret = ret && (xreal[i] == -yreal[i])&&(ximag[i] == -yimag[i]);
        if(ret == true) return true;
        
        ret = true;
        for(int i = 0; i < xreal.length; i++)
            ret = ret && (xreal[i] == -yimag[i])&&(ximag[i] == yreal[i]);
        if(ret == true) return true;
        
        ret = true;
        for(int i = 0; i < xreal.length; i++)
            ret = ret && (xreal[i] == yimag[i])&&(ximag[i] == -yreal[i]);
        if(ret == true) return true;
            
        return false;
    }
}
