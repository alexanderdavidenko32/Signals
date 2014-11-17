package main.wavelet.impl;

import main.wavelet.Wavelet;

/**
 * Created by chrno on 11/2/14.
 */
public class Gaus3PWavelet extends Wavelet {

    public Gaus3PWavelet(int fd, int fw) {
        super(fd, fw);
        calculateWaveletValues();
    }

    private double k = 2.22;

    @Override
    public double calculate(double t) {
        return ( Math.pow(t, 3) - 3 * t ) * calculateExpPart(t);
    }

    @Override
    public double getK() {
        return k;
    }
}
