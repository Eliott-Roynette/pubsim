/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simulator.poly;

/**
 * 
 * @author Robby McKilliam
 */
public interface PolynomialPhaseEstimator{
    
    /** Set the number of samples */
    public void setSize(int n);

    /** 
     * Return the order of this estimator.
     * This is the number of parameters.
     */
    public int getOrder();
    
    /** 
     * Run the estimator on recieved real and imaginary signal.
     * Returns an array of polynomial parameters.
     */
    public double[] estimate(double[] real, double[] imag);

    /**
     * Run the estimator and return the square error
     * wrapped modulo the the ambiguity region between
     * the estimate and the truth.
     */
    public double[] error(double[] real, double[] imag, double[] truth);
    
}
