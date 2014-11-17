package main.wavelet.impl;

import main.wavelet.Wavelet;


/**
 * Created by chrno on 10/26/14.
 */
public class MexicanHatWavelet extends Wavelet {
    public MexicanHatWavelet() {
    }

    public MexicanHatWavelet(int fd, int fw) {
        super(fd, fw);
        calculateWaveletValues();
    }

    private double k = 1.816;

    /**
     * @param t текущее значение для t (значение по оси x)
     */
    @Override
    public double calculate(double t) {
        return (1. - t * t) * calculateExpPart(t);
    }

    @Override
    public double getK() {
        return k;
    }
}
