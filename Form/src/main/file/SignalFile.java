package main.file;

import java.util.ArrayList;

/**
 * Created by chrno on 10/18/14.
 */
public class SignalFile {
    private String signature;
    private int channelsCount;
    private int pointsCountAtTheSameTime; //N
    private int spectralLinesCount; // N/2
    private int cutOffFrequency;
    private float frequencyResolution;
    private float timeToReceive; // 1/frequencyResolution
    private int totalTimeToReceive; //in seconds
    private int userBlocksCount;
    private int dataSize;
    private int systemBlocksCount;
    private float maxValue;
    private float minValue;

    private ArrayList<Float> signals;

    public SignalFile() {
        this.signature = "";
        this.channelsCount = 0;
        this.pointsCountAtTheSameTime = 0;
        this.spectralLinesCount = 0;
        this.cutOffFrequency = 0;
        this.frequencyResolution = 0;
        this.timeToReceive = 0;
        this.totalTimeToReceive = 0;
        this.userBlocksCount = 0;
        this.dataSize = 0;
        this.systemBlocksCount = 0;
        this.maxValue = 0;
        this.minValue = 0;
        this.signals = new ArrayList<>();
    }

    public SignalFile(String signature, int channelsCount, int pointsCountAtTheSameTime, int spectralLinesCount, int cutOffFrequency, float frequencyResolution, float timeToReceive, int totalTimeToReceive, int userBlocksCount, int dataSize, int systemBlocksCount, float maxValue, float minValue, ArrayList<Float> signals) {
        this.signature = signature;
        this.channelsCount = channelsCount;
        this.pointsCountAtTheSameTime = pointsCountAtTheSameTime;
        this.spectralLinesCount = spectralLinesCount;
        this.cutOffFrequency = cutOffFrequency;
        this.frequencyResolution = frequencyResolution;
        this.timeToReceive = timeToReceive;
        this.totalTimeToReceive = totalTimeToReceive;
        this.userBlocksCount = userBlocksCount;
        this.dataSize = dataSize;
        this.systemBlocksCount = systemBlocksCount;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.signals = signals;
    }

    @Override
    public String toString() {
        return "Сигнатура: '" + signature + "\' \r\n" +
                "Количество каналов: " + channelsCount + "\r\n" +
                "Размер выборки на один канал: " + pointsCountAtTheSameTime + "\r\n" +
                "Количество спектральных линий: " + spectralLinesCount  + "\r\n" +
                "Частота среза: " + cutOffFrequency  + "\r\n" +
                "Частотное разрешение: " + frequencyResolution + "\r\n" +
                "Время приёма блока данных: " + timeToReceive + "\r\n" +
                "Общее время приёма данных: " + totalTimeToReceive + "\r\n" +
                "Количество принятых блоков (задано пользователем): " + userBlocksCount + "\r\n" +
                "размер данных: " + dataSize + "\r\n" +
                "число принятых блоков(принято системой): " + systemBlocksCount + "\r\n" +
                "максимальное значение принятых данных: " + maxValue + "\r\n" +
                "минимальное значение принятых данных:" + minValue + "\r\n";
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getChannelsCount() {
        return channelsCount;
    }

    public void setChannelsCount(int channelsCount) {
        this.channelsCount = channelsCount;
    }

    public int getPointsCountAtTheSameTime() {
        return pointsCountAtTheSameTime;
    }

    public void setPointsCountAtTheSameTime(int pointsCountAtTheSameTime) {
        this.pointsCountAtTheSameTime = pointsCountAtTheSameTime;
    }

    public int getSpectralLinesCount() {
        return spectralLinesCount;
    }

    public void setSpectralLinesCount(int spectralLinesCount) {
        this.spectralLinesCount = spectralLinesCount;
    }

    public int getCutOffFrequency() {
        return cutOffFrequency;
    }

    public void setCutOffFrequency(int cutOffFrequency) {
        this.cutOffFrequency = cutOffFrequency;
    }

    public float getFrequencyResolution() {
        return frequencyResolution;
    }

    public void setFrequencyResolution(float frequencyResolution) {
        this.frequencyResolution = frequencyResolution;
    }

    public float getTimeToReceive() {
        return timeToReceive;
    }

    public void setTimeToReceive(float timeToReceive) {
        this.timeToReceive = timeToReceive;
    }

    public int getTotalTimeToReceive() {
        return totalTimeToReceive;
    }

    public void setTotalTimeToReceive(int totalTimeToReceive) {
        this.totalTimeToReceive = totalTimeToReceive;
    }

    public int getUserBlocksCount() {
        return userBlocksCount;
    }

    public void setUserBlocksCount(int userBlocksCount) {
        this.userBlocksCount = userBlocksCount;
    }

    public int getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    public int getSystemBlocksCount() {
        return systemBlocksCount;
    }

    public void setSystemBlocksCount(int systemBlocksCount) {
        this.systemBlocksCount = systemBlocksCount;
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

    public ArrayList<Float> getSignals() {
        return signals;
    }

    public void setSignals(ArrayList<Float> signals) {
        this.signals = signals;
    }

    public void addSignal(Float signal) {
        this.signals.add(signal);
    }
}
