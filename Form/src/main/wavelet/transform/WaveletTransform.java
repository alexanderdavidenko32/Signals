package main.wavelet.transform;

import main.wavelet.Wavelet;

import java.util.ArrayList;

/**
 * Created by chrno on 10/26/14.
 */
public class WaveletTransform {

    Wavelet wavelet;
    public WaveletTransform() {
    }

    public WaveletTransform(Wavelet wavelet) {
        this.wavelet = wavelet;
    }

    public double calculateTransform(double tau, ArrayList<Float> signalValues) {
        double sum = 0;
        double scale =  wavelet.getScale();
        double sqrtScale = 1./Math.sqrt(scale);
//        double sqrtScale = 1./scale;

        for (int t = 0; t < 1000; t++) {
//            double argument = (8. * ((double)n - m) - 4. * Nmhat) / Nmhat;
            double argument = (t - tau) / scale;

            double waveletResult = wavelet.calculate(argument);

            sum += (double)signalValues.get(t) * waveletResult;
        }

        return  sqrtScale * sum;
    }
}