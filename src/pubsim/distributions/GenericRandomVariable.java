/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pubsim.distributions;

import Jama.Matrix;
import java.io.Serializable;

/**
 *
 * @author harprobey
 */
public interface GenericRandomVariable<T> extends NoiseGenerator<T> {

    public T getMean();
    public Matrix getVariance();

    /** Return the pdf evaluate at x */
    public double pdf(T x);

    /** Return the cumulative distribution function evaluated at x */
    public double cdf(T x);

    /**
     * Return the inverse cumulative distribution function.
     * This allows get noise to work in a standard way by generating
     * uniform noise in [0,1] and applying icdf.
     * However, you don't have to implement this if you have a better
     * way of generating the noise.
     */
    public T icdf(double x);

}
