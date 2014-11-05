package main.component;

import main.file.SignalFile;
import main.geometry.*;
import main.geometry.Point;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by chrno on 10/18/14.
 */
public class GraphicsPanel extends JPanel {

    private float maxValue;

    private float minValue;

    private List<Line> lines;

    private SignalFile signalFile;

    private List<Line> waveletLines;

    private List<Float> waveLetValues;

    public GraphicsPanel() {
        maxValue = 0;
        minValue = 0;
        lines = new ArrayList<Line>();
        signalFile = new SignalFile();
        waveletLines = new ArrayList<>();
        waveLetValues = new ArrayList<>();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        drawAxis(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.GREEN);

        calculateLines();

        int i = 0;
        for (Line line : lines) {
            g2d.draw(line);
            if (i > 816) {
                g2d.setColor(Color.RED);
            }
            i++;
        }

        g2d.setColor(Color.PINK);
        for (Line waveletLine : waveletLines) {
            g2d.draw(waveletLine);
        }
    }

    private void drawAxis(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        ArrayList<Float> signals = signalFile.getSignals();

        if (signals.size() > 0) {
            g2d.setColor(Color.GRAY);

            int scrollBarHeight = 1;
//        int height = this.getHeight();
            int height = this.getParent().getHeight();
            int width = this.getWidth();
//        ArrayList<Float> signals = signalFile.getSignals();
            float newPanelHeight = Math.abs(getMaxValue()) + Math.abs(getMinValue());

            // коэффициент для масштабирования графика
            float factor = (height - scrollBarHeight) / newPanelHeight;

            float xAxis = signalFile.getMaxValue() * factor;

            ArrayList<AxisLine> axisLines = new ArrayList<>();

            AxisLine xLine = new AxisLine(0, xAxis, width, xAxis, "0", new Point(0, xAxis));
            axisLines.add(xLine);

            float aboveXMaxValueSegment = Math.abs(getMaxValue()) / 3;

            for (int i = 0; i < aboveXMaxValueSegment; i++) {
                float realYOffset = aboveXMaxValueSegment * i;
                float yOffset = xAxis - aboveXMaxValueSegment * i * factor;

                String description = String.valueOf(Math.round(realYOffset));
                Point point = new Point(0, yOffset - 1);

                AxisLine axisLine = new AxisLine(0, yOffset, width, yOffset, description, point);
                axisLines.add(axisLine);
            }

            float belowMaxValueSegment = Math.abs(getMinValue()) / 3;
            for (int i = 0; i < belowMaxValueSegment; i++) {
                float realYOffset = -belowMaxValueSegment * i;
                float yOffset = xAxis + belowMaxValueSegment * i * factor;

                String description = String.valueOf(Math.round(realYOffset));
                Point point = new Point(0, yOffset - 2);

                AxisLine axisLine = new AxisLine(0, yOffset, width, yOffset, description, point);
                axisLines.add(axisLine);
            }

            for (AxisLine axisLine : axisLines) {
                Line line = new Line(axisLine.getX1(), axisLine.getY1(), axisLine.getX2(), axisLine.getY2());
                g2d.draw(line);
                g2d.drawString(axisLine.getDescription(), (float) axisLine.getDescriptionPoint().getX(), (float) axisLine.getDescriptionPoint().getY());
            }
        }
    }

    private void calculateLines() {
        ArrayList<Float> signals = signalFile.getSignals();

        if (signals.size() > 0) {
            this.setLines(new ArrayList<Line>());
            this.setWaveletLines(new ArrayList<Line>());

            int scrollBarHeight = 1;
            //исходная высота панели
//            int initialPanelHeight = this.getHeight();
            int initialPanelHeight = this.getParent().getHeight();

            // считаем новые параметры для панели
            float newPanelHeight = Math.abs(getMaxValue()) + Math.abs(getMinValue());

            // хитрый расчет коэффициента для смещеиния по оси x
            Float timeInIteration = (float)signalFile.getTotalTimeToReceive() / (float)signalFile.getDataSize() *  (float)4086;
//            Float timeInIteration = panelWidth / 4086;

            // коэффициент для масштабирования графика
            // тут происходит непонятный мейджик. без  scrollBarHeight
            // отличного от нуля не работает масштабирование для некоторых сигналов
            float factor = (initialPanelHeight - scrollBarHeight) / newPanelHeight;
//            float factor = 1;

            float xAxis = getMaxValue() * factor;
            int yAxis = 0;

            /**
             * на -1 умножаем для того, чтобы инвертировать значение по y. иначе у нас будет получаться график наоборот.
             * например
             * (signals.get(i) * factor) == 100 (выше оси x)
             * (signals.get(i) * factor) + xAxis  == 100 + 150  (отрисуется ниже оси x, потому что xAxis - отступ сверху до оси x)
             * (signals.get(i) * factor) * -1 + xAxis == -100 + 150 = 50 (отрисуется сверху на 50 пикселей, и выше на 100 от оси x)
             */
            for (int i = 0; i < signals.size() - 2; i++) {
                lines.add(new Line(i * timeInIteration, (signals.get(i) * factor) * -1 + xAxis, (i + 1) * timeInIteration, (signals.get(i + 1) * factor) * -1 + xAxis));
            }

            for (int i = 0; i < waveLetValues.size() - 2; i++) {
                waveletLines.add(new Line(i * timeInIteration, (waveLetValues.get(i) * factor) * -1 + xAxis, (i + 1) * timeInIteration, (waveLetValues.get(i + 1) * factor) * -1 + xAxis));
            }
        }
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

    public List<Line> getLines() {
        return lines;
    }

    public void addLine(Line line) {
        lines.add(line);
    }

    public void setLines(ArrayList<Line> lines) {
        this.lines = lines;
    }

    public SignalFile getSignalFile() {
        return signalFile;
    }

    public void setSignalFile(SignalFile signalFile) {
        this.signalFile = signalFile;
    }

    public List<Float> getWaveLetValues() {
        return waveLetValues;
    }

    public void setWaveLetValues(List<Float> waveLetValues) {
        this.waveLetValues = waveLetValues;
    }

    public List<Line> getWaveletLines() {
        return waveletLines;
    }

    public void setWaveletLines(List<Line> waveletLines) {
        this.waveletLines = waveletLines;
    }
}
