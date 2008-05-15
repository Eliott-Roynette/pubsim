/*
 * PATSymbol.java
 *
 * Created on 27 November 2007, 11:59
 */

package simulator.qam.pat;

import simulator.Complex;

/**
 * Functions for any class that has a PAT symbol
 * @author Robby McKilliam
 */
public interface PATSymbol {
    
    public void setPATSymbol(double real, double imag);
    
    public void setPATSymbol(Complex c);
    
    public Complex getPATSymbol();
    
}
