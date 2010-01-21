/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simulator.fes;

import distributions.GaussianNoise;
import distributions.NoiseGenerator;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author robertm
 */
public class RunComputationalTrials {

    public static void main(String[] args) throws Exception  {

        int n = 1024;
        double pha = 0.1;
        double f = 0.1;
        int seed = 26;
        int iterations = 1;

        String nameetx = Integer.toString(n);

        NoisyComplexSinusoid signal_gen = new NoisyComplexSinusoid();
        signal_gen.setFrequency(f);
        signal_gen.setSize(n);
        signal_gen.setPhase(pha);
        NoiseGenerator noise = new GaussianNoise();
        signal_gen.setNoiseGenerator(noise);

        double from_log_snr = 20.0;
        double to_log_snr = -20.0;
        double step_log_snr = -2;

        Vector<Double> snr_array = new Vector<Double>();
        Vector<Double> snr_db_array = new Vector<Double>();
        for(double snrdb = from_log_snr; snrdb > to_log_snr; snrdb += step_log_snr){
            snr_db_array.add(new Double(snrdb));
            snr_array.add(new Double(Math.pow(10.0, ((snrdb)/10.0))));
        }

        Vector<FrequencyEstimator> estimators = new Vector<FrequencyEstimator>();

        //add the estimators you want to run
        estimators.add(new ZnLLS());
        //estimators.add(new PeriodogramFFTEstimator(4));
        //estimators.add(new SamplingLatticeEstimator(12*n));
        //estimators.add(new KaysEstimator());
        //estimators.add(new PSCFDEstimator());
        //estimators.add(new QuinnFernades());

        Iterator<FrequencyEstimator> eitr = estimators.iterator();
        while(eitr.hasNext()){

            FrequencyEstimator est = eitr.next();

            Vector<Double> mse_array = new Vector<Double>(snr_array.size());
            java.util.Date start_time = new java.util.Date();
            Random r = new Random();
            for(int i = 0; i < snr_array.size(); i++){

                noise.setVariance(0.5/snr_array.get(i));
                for(int ii = 0; i < iterations; i++){

                    f = r.nextDouble() - 0.5;
                    pha = r.nextDouble() - 0.5;
                    signal_gen.setFrequency(f);
                    signal_gen.setPhase(pha);

                    signal_gen.generateReceivedSignal();

                    double fhat = est.estimateFreq(signal_gen.getReal(), signal_gen.getImag());


                }

            }
            java.util.Date end_time = new java.util.Date();
            System.out.println(est.getClass().getName() +
                    " completed in " +
                    (end_time.getTime() - start_time.getTime())/1000.0
                    + "seconds");

        }

    }

}
