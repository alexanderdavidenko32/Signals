package main.wavelet.impl;

import main.wavelet.Wavelet;

/**
 * Created by chrno on 11/2/14.
 */
public class Gaus4PWavelet extends Wavelet {
    public Gaus4PWavelet(int fd, int fw) {
        super(fd, fw);
        calculateWaveletValues();
    }

    private double k = 2.25;

    @Override
    public double calculate(double t) {
        return (Math.pow(t, 4) - 6 * Math.pow(t, 2) + 3) * calculateExpPart(t);
    }

    @Override
    public double getK() {
        return k;
    }
}
