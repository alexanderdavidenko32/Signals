package main.spectrum;

import java.util.ArrayList;

/**
 * Created by chrno on 11/17/14.
 */
public class Spectrum {
    private int baseSignalSize;
    private int spectrumSize;
    private ArrayList<Float> aSin;
    private ArrayList<Float> aCos;
    private ArrayList<Float> resultSpectrum;
    private ArrayList<Float> signals;
    private float maxValue;
    private float minValue;

    public Spectrum(ArrayList<Float> signals) {
        this.signals = signals;
//        baseSignalSize = signals.size();
        baseSignalSize = 1024;
        spectrumSize = baseSignalSize / 2;
        maxValue = 0;
        minValue = 0;
        aSin = new ArrayList<>();
        aCos = new ArrayList<>();
        resultSpectrum = new ArrayList<>();
    }

    private void calculateASin() {
        aSin = new ArrayList<>();
        for (int j = 0; j < spectrumSize; j++) {
            float sum = 0;
            for (int i = 0; i < baseSignalSize; i++) {
                sum += signals.get(i) * Math.sin((2F * Math.PI * i * j) / baseSignalSize);
            }
            float result = ( 2F / (float)baseSignalSize ) * sum;
            aSin.add(result);
        }
    }

    private void calculateACos() {
        aCos = new ArrayList<>();
        for (int j = 0; j < spectrumSize; j++) {
            float sum = 0;
            for (int i = 0; i < baseSignalSize; i++) {
                sum += signals.get(i) * Math.cos((2 * Math.PI * i * j) / baseSignalSize);
            }
            float result = ( 2F / (float)baseSignalSize ) * sum;
            aCos.add(result);
        }
    }

    public ArrayList<Float> calculateSpectrum() {
        calculateASin();
        calculateACos();

        resultSpectrum = new ArrayList<>();
        for (int j = 0; j < spectrumSize; j++) {
            double result = Math.sqrt( Math.pow(aSin.get(j), 2) + Math.pow(aCos.get(j), 2) );
            resultSpectrum.add((float)result);

            if ((float)result > maxValue) {
                maxValue = (float)result;
            }
            if ((float)result < minValue) {
                minValue = (float)result;
            }
        }
        return resultSpectrum;
    }

    public ArrayList<Float> getResultSpectrum() {
        return resultSpectrum;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    public float getMinValue() {
        return minValue;
    }

    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }
}
