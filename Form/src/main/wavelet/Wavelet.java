package main.wavelet;

import java.util.ArrayList;

/**
 * Created by chrno on 10/26/14.
 */
public abstract class Wavelet implements IWavelet {
    /**
     * Ширина вейвлета (N)
     */
    protected int width;

    /**
     * коэффициент для рассчета ширины вейвлета
     */
    private double k;

    /**
     * Частота дескритизации.
     * нужна для рассчета ширины вейвлета
     */
    protected int Fd;

    /**
     * частота для которой определяется вейвлет
     * нужна для рассчета ширины вейвлета
     * (например 50 Гц)
     */
    protected int Fw;

    /**
     * рассчтианные значения вейвлета
     */
    protected ArrayList<Double> values;

    public Wavelet() {
    }

    protected Wavelet(int fd, int fw) {
        Fd = fd;
        Fw = fw;
    }

    public double calculateExpPart(double t) {
        return Math.exp( -(t*t/2.) );
    }

    public int calculateWaveletWidth() {
        return (int)Math.round(getK() * Fd / Fw);
    }

    public ArrayList<Double> calculateWaveletValues() {
        width = calculateWaveletWidth();

        values = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            values.add(calculate(i));
        }
        return values;
    }

    public int getWidth() {
        return width;
    }

    public double getK() {
        return k;
    }

    public ArrayList<Double> getValues() {
        return values;
    }
}
