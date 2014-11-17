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

    public double calculateTransform(int startIndex, ArrayList<Float> signalValues) {
        double sum = 0;
        ArrayList<Double> waveletValues = wavelet.getValues();

        for (int t = 0; t < wavelet.getWidth(); t++) {

            if (startIndex + t + 1 > signalValues.size()) {
                return sum;
            }
            sum += (double)signalValues.get(startIndex + t) * waveletValues.get(t);
        }

        return sum;
    }
}