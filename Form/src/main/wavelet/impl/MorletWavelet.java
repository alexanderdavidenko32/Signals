package main.wavelet.impl;

import main.wavelet.Wavelet;

/**
 * Created by chrno on 11/2/14.
 */
public class MorletWavelet extends Wavelet {
    private double scale = .6;

    @Override
    public double calculate(double t) {
        return Math.cos(2 * Math.PI * t) * calculateExpPart(t);
    }

    @Override
    public double getScale() {
        return scale;
    }
}
