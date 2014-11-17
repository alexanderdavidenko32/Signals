package main.wavelet.impl;

import main.wavelet.Wavelet;

/**
 * Created by chrno on 11/2/14.
 */
public class MorletWavelet extends Wavelet {
    public MorletWavelet(int fd, int fw) {
        super(fd, fw);
        calculateWaveletValues();
    }

    private double k = 8;


    @Override
    public double calculate(double t) {
        return Math.cos(2 * Math.PI * t) * calculateExpPart(t);
    }

    @Override
    public double getK() {
        return k;
    }
}
