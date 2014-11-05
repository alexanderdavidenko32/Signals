package main.wavelet.impl;

import main.wavelet.Wavelet;

/**
 * Created by chrno on 11/2/14.
 */
public class Gaus3PWavelet extends Wavelet {
    private double scale = 10;

    @Override
    public double calculate(double t) {
        return ( Math.pow(t, 3) - 3 * t ) * calculateExpPart(t);
    }
    @Override
    public double getScale() {
        return scale;
    }
}
