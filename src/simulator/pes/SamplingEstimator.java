package simulator.pes;

import simulator.*;

/**
 * A modification of the Bresenham estimator which samples the line
 * segment rather than tracing through all the Voronoi cell boundaries.
 * @author Vaughan Clarkson, 16-Jun-05.
 * Add calculateObjective method, 13-Jan-07.
 */
public class SamplingEstimator extends Anstar implements PRIEstimator {

    protected int NUM_SAMPLES = 100;
    
    public SamplingEstimator(){
    }
    
    public SamplingEstimator(int samples){
        NUM_SAMPLES = samples;
    }

    double[] zeta, fzeta, kappa;

    public void setSize(int N) {
	setDimension(N-1); // => n = N-1
	zeta = new double[N];
	fzeta = new double[N];
	kappa = new double[N];
    }

    double calculateObjective(double[] y, double f) {
	project(y, zeta);
	for (int i = 0; i <= n; i++)
	    fzeta[i] = f * zeta[i];
	nearestPoint(fzeta);
	double L = 0;
	for (int i = 0; i <= n; i++) {
	    double diff = zeta[i] - (v[i] / f);
	    L += diff * diff;
	}
	return L;
    }

    public double estimateFreq(double[] y, double fmin, double fmax) {
	if (n != y.length-1)
	    setSize(y.length);
	project(y, zeta);
	double bestL = Double.POSITIVE_INFINITY;
	double fhat = fmin;
	double fstep = (fmax - fmin) / NUM_SAMPLES;
	for (double f = fmin; f <= fmax; f += fstep) {
	    for (int i = 0; i <= n; i++)
		fzeta[i] = f * zeta[i];
	    nearestPoint(fzeta);
	    double sumv2 = 0, sumvz = 0;
	    for (int i = 0; i <= n; i++) {
		sumv2 += v[i] * v[i];
		sumvz += v[i] * zeta[i];
                //sumv2 += v[i] * zeta[i];
		//sumvz += zeta[i] * zeta[i];
	    }
	    double f0 = sumv2 / sumvz;
	    double L = 0;
	    for (int i = 0; i <= n; i++) {
		double diff = zeta[i] - (v[i] / f0);
                //double diff = f0*zeta[i] - v[i];
		L += diff * diff;
	    }
	    if (L < bestL) {
		bestL = L;
		fhat = f0;
	    }
	}
	return fhat;
    }
    
    /** Return the likilhood of the best result */ 
    public double likelihood(double[] y, double fmin, double fmax) {
	if (n != y.length-1)
	    setSize(y.length);
	project(y, zeta);
	double bestL = Double.POSITIVE_INFINITY;
	double fhat = fmin;
	double fstep = (fmax - fmin) / NUM_SAMPLES;
	for (double f = fmin; f <= fmax; f += fstep) {
	    for (int i = 0; i <= n; i++)
		fzeta[i] = f * zeta[i];
	    nearestPoint(fzeta);
	    double sumv2 = 0, sumvz = 0;
	    for (int i = 0; i <= n; i++) {
		sumv2 += v[i] * v[i];
		sumvz += v[i] * zeta[i];
	    }
	    double f0 = sumv2 / sumvz;
	    double L = 0;
	    for (int i = 0; i <= n; i++) {
		double diff = zeta[i] - (v[i] / f0);
                //double diff = f0*zeta[i] - v[i];
		L += diff * diff;
	    }
	    if (L < bestL) {
		bestL = L;
		fhat = f0;
	    }
	}
	return bestL;
    }
    
        /** Return the likilhood of the best result */ 
    public double[] bestLatticePoint(double[] y, double fmin, double fmax) {
	if (n != y.length-1)
	    setSize(y.length);
        double[] bestv = new double[y.length];
	project(y, zeta);
	double bestL = Double.POSITIVE_INFINITY;
	double fhat = fmin;
	double fstep = (fmax - fmin) / NUM_SAMPLES;
	for (double f = fmin; f <= fmax; f += fstep) {
	    for (int i = 0; i <= n; i++)
		fzeta[i] = f * zeta[i];
	    nearestPoint(fzeta);
	    double sumv2 = 0, sumvz = 0;
	    for (int i = 0; i <= n; i++) {
		sumv2 += v[i] * v[i];
		sumvz += v[i] * zeta[i];
	    }
	    double f0 = sumv2 / sumvz;
	    double L = 0;
	    for (int i = 0; i <= n; i++) {
		//double diff = zeta[i] - (v[i] / f0);
                double diff = f0*zeta[i] - v[i];
		L += diff * diff;
	    }
	    if (L < bestL) {
		bestL = L;
		fhat = f0;
                bestv = v.clone();
	    }
	}
	return bestv;
    }

    public double varianceBound(double sigma, double[] k) {
	Anstar.project(k, kappa);
	double sk = 0;
	for (int i = 0; i < k.length; i++)
	    sk += kappa[i] * kappa[i];
	return sigma * sigma / sk;
    }
}