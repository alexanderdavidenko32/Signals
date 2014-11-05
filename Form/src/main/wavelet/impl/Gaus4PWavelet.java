package main.wavelet.impl;

import main.wavelet.Wavelet;

/**
 * Created by chrno on 11/2/14.
 */
public class Gaus4PWavelet extends Wavelet {
    private double scale = .55;
    @Override
    public double calculate(double t) {
        return (Math.pow(t, 4) - 6 * Math.pow(t, 2) + 3) * calculateExpPart(t);
    }
    @Override
    public double getScale() {
        return scale;
    }
}
