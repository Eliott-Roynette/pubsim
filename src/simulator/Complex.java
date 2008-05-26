/*
 * Complex.java
 *
 * Created on 11 October 2007, 10:53
 */

package simulator;

/**
 * Basic complex functions.  I nicked this from 
 * http://www.cs.princeton.edu/introcs/97data/Complex.java.html 
 * <p>
 * @author Robby McKilliam
 */
public class Complex extends Object{
    
    private double re;   // the real part
    private double im;   // the imaginary part

    /** create a new object with the given real and imaginary parts */
    public Complex(double real, double imag) {
        re = real;
        im = imag;
    }

    /** Compy constructor */
    public Complex(Complex x) {
        re = x.re;
        im = x.im;
    }
    
    /** Default constructor has zero real and imaginary components */
    public Complex() {
        re = 0;
        im = 0;
    }
    
    /** Set the real and imaginary parts */
    public void set(double re, double im){
        this.re = re;
        this.im = im;
    }
    
    /** Copy the real and imaginary parts */
    public void copy(Complex c){
        this.re = c.re;
        this.im = c.im;
    }
    
    /** return a string representation of the invoking Complex object */
    @Override
    public String toString() {
        if (im == 0) return re + "";
        if (re == 0) return im + "i";
        if (im <  0) return re + " - " + (-im) + "i";
        return re + " + " + im + "i";
    }

    /** return abs/modulus/magnitude */
    public double abs()   { return Math.hypot(re, im); }  // Math.sqrt(re*re + im*im)
    
    /** Return the angle/phase/argument */
    public double phase() { return Math.atan2(im, re); }  // between -pi and pi
    
    /** Return abs/modulus/magnitude */
    public double abs2()   { return re*re + im*im; }

    /** return a new Complex object whose value is (this + b) */
    public Complex plus(Complex b) {
        return new Complex(re + b.re, im + b.im);
    }

    /** return a new Complex object whose value is (this - b) */
    public Complex minus(Complex b) {
        return new Complex(re - b.re, im - b.im);
    }

    /** return a new Complex object whose value is (this * b) */
    public Complex times(Complex b) {
        Complex a = this;
        double real = a.re * b.re - a.im * b.im;
        double imag = a.re * b.im + a.im * b.re;
        return new Complex(real, imag);
    }

    /** 
     * scalar multiplication. <br>
     * return a new object whose value is (this * alpha)
     */
    public Complex times(double alpha) {
        return new Complex(alpha * re, alpha * im);
    }

    /** return a new Complex object whose value is the conjugate of this */
    public Complex conjugate() {  return new Complex(re, -im); }

    /** return a new Complex object whose value is the reciprocal of this */
    public Complex reciprocal() {
        double scale = re*re + im*im;
        return new Complex(re / scale, -im / scale);
    }

    /** return the real or imaginary part */
    public double re() { return re; }
    public double im() { return im; }

    /** return a / b */
    public Complex divides(Complex b) {
        Complex a = this;
        return a.times(b.reciprocal());
    }

    /** 
     * return a new Complex object whose 
     * value is the complex exponential of this
     */
    public Complex exp() {
        return new Complex(Math.exp(re) * Math.cos(im), 
                Math.exp(re) * Math.sin(im));
    }

    /** return a new Complex object whose value is the complex sine of this */
    public Complex sin() {
        return new Complex(Math.sin(re) * Math.cosh(im), 
                Math.cos(re) * Math.sinh(im));
    }

    /** return a new Complex object whose value is the complex cosine of this */
    public Complex cos() {
        return new Complex(Math.cos(re) * Math.cosh(im), 
                -Math.sin(re) * Math.sinh(im));
    }

    /** return a new Complex object whose value is the tangent of this */
    public Complex tan() {
        return sin().divides(cos());
    }
    
    /** a static version of plus */
    public static Complex plus(Complex a, Complex b) {
        double real = a.re + b.re;
        double imag = a.im + b.im;
        Complex sum = new Complex(real, imag);
        return sum;
    }
    
    /** Test if this complex number is equal to c */
    public boolean equals(Complex c){
        return c.re == re && c.im == im;
    }
    
}
