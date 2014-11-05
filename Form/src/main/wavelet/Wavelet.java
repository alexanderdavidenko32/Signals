package main.wavelet;

/**
 * Created by chrno on 10/26/14.
 */
public abstract class Wavelet implements IWavelet {
    public Wavelet() {
    }

    public double calculateExpPart(double t) {
        return Math.exp( -(t*t/2.) );
    }

    @Override
    public double calculate(double t) {
        return 0;
    }

    @Override
    public double getScale() {
        return 0;
    }
}
