package main.wavelet.impl;

import main.wavelet.Wavelet;

/**
 * Created by chrno on 10/26/14.
 */
public class MexicanHatWavelet extends Wavelet {
    public MexicanHatWavelet() {
    }

    private double scale = .35;

    /**
     * @param t текущее значение для t (значение по оси x)
     */
    @Override
    public double calculate(double t) {
//        double squaredBrackets = Math.pow(((8 * n - 4 * this.N) / this.N), 2);
//        double result = (1 - squaredBrackets) * Math.pow(Math.E, -(squaredBrackets/2));
//        double result = (1 - squaredBrackets) * Math.exp( -(squaredBrackets/2) );
        return (1. - t * t) * calculateExpPart(t);
    }
//
//    public double calculateNormalizedWavelet(double n, double N) {
//        double summ = 0;
//
//        for (int i = 0; i < N; i++) {
//            summ += calculateWavelet(i);
//        }
//        double result = calculateWavelet(n) / (1.32 * summ);
//
//        return  result;
//    }

    @Override
    public double getScale() {
        return scale;
    }
}
