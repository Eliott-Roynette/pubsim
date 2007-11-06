/*
 * Pnx.java
 *
 * Created on 2 November 2007, 13:24
 */

package lattices;

import simulator.VectorFunctions;

/**
 * Class that solves the nearst point algorithm for the family of lattices
 * P_n^alpha.  This is a suboptimal sampling approach.  It is polynomial time 
 * in n but exponential in alpha.
 * @author Robby McKilliam
 */
public class Pna implements LatticeNearestPointAlgorithm{
    
    protected int a;
    protected double[] u, v, g, yt, yp;
    protected int n;
    
    /** Must set a when constucted */
    public Pna(int a){
        this.a = a;
        //this is a bit icky.  It's just to
        //ensure that it's not a null pointer
        //for nearestPoint
        u = new double[0];
    }
    
    /** Sets a and the dimension n when constructed */
    public Pna(int a, int n){
        this.a = a;
        setDimension(n);
    }
    
    public void setDimension(int n){
        this.n =  n;
        u = new double[n + a];
        v = new double[n + a];
        yt = new double[n + a];
        yp = new double[n + a];
        g = createg(n,a);
    }
    
    public void nearestPoint(double[] y){
        if(u.length != y.length)
            setDimension(y.length-a);
        
        //project point into this space of lattice P_n^a
        project(y, yp, a);
        
        double Dmin = Double.POSITIVE_INFINITY;
        if(a > 0){       
            double magg = VectorFunctions.magnitude(g);
            double step = magg/Math.pow(n,a);
            Pna pna = new Pna(a-1);
            pna.setDimension(n);
            for(double s = 0; s < magg; s+=step){
                for(int i = 0; i < y.length; i++)
                    yt[i] = y[i] + s*g[i];
                pna.nearestPoint(yt);
                //this is bad practice, I am reusing memory
                project(pna.getIndex(), pna.getLatticePoint(), a);
                double D = VectorFunctions.distance_between2(yp, 
                                                    pna.getLatticePoint());
                if(D < Dmin){
                    Dmin = D;
                    System.arraycopy(pna.getIndex(), 0, u, 0, u.length);
                }
            }
        }else{
            for(int i = 0; i < y.length; i++)
                u[i] = Math.round(y[i]);
        }
        
        project(u, v, a);
        
    }
    
    public double[] getLatticePoint() { return v; }
    
    public double[] getIndex() { return u; }
    
    /** creates the orthogonal vector for Pna.  Allocates memory*/
    protected static double[] createg(int n, int a){
        double[] g = new double[n+a];
        if( a > 0 ){
            for(int i = 0; i < n+a; i++)
                g[i] = Math.pow(i+1,a-1);      

            project(g,g,a-1);
        }
        return g;
    }
    
    /** 
     * Project x into the space of the lattice Pna and return
     * the projection into y.  Requires recursion, runs in O(n a) time
     * PRE: x.length = y.length
     */
    public static void project(double[] x, double[] y, int a){
        if(a > 0){
            project(x,y,a-1);
            double[] g = createg(x.length-a, a);
            VectorFunctions.projectOrthogonal(g,y,y);
        }
        else{
            System.arraycopy(x, 0, y, 0, x.length);
        }
    }
    
    protected double[][] rotmat;
    /** Returns a rotation matrix generated by generateRotationMatrix(n,a)*/
    public double[][] getRotationMatrix(){
        if( rotmat == null )
            rotmat = generateRotationMatrix(n,a);
        return rotmat;
    }
    
    /**
     * Generates a rotation matrix that rotates a vector from R^(n-a)
     * to R^n such that it lies in the space of the lattice P_n_a.
     * <p>
     * This is usefull, in particular, for testing the performance
     * of these lattices in Gaussian noise.
     * <p>
     * This could be done using a QR decompostition, however it
     * potentially requires lots of matrix multiplication first and
     * tends to be unstable.  Instead, we use a coordinate change
     * followed by a stable gram-schmidt algorithm.
     */
    public static double[][] generateRotationMatrix(int n, int a){
        //store all the g's first, otherwise
        //this can take a long time to run.
        double[][] gs = new double[a+1][n+a];
        for(int i = 1; i <= a; i++)
            System.arraycopy(createg(n+a-i,i), 0, gs[i], 0, n+a);
        
        //stable version of gram schmit othonormalisation
        double[][] mat = new double[n][n+a];
        for(int i = 0; i < n; i++){
            mat[i][i] = 1.0;  //make column this e_i
            for(int j = 1; j <= a; j++)
                VectorFunctions.projectOrthogonal(gs[j], mat[i], mat[i]);
            for(int j = 0; j < i; j++)
                VectorFunctions.projectOrthogonal(mat[j], mat[i], mat[i]);
            VectorFunctions.normalise(mat[i], mat[i]);
        }
        
        return VectorFunctions.transpose(mat);
    }

    /** {@inheritDoc} */
    public double volume(){
        return 0;
    }
    
}
