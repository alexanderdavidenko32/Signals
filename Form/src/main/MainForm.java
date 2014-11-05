package main;

import main.component.GraphicsPanel;
import main.file.SignalFile;
import main.file.reader.BufferFileReader;
import main.geometry.Line;
import main.wavelet.*;
import main.wavelet.impl.Gaus3PWavelet;
import main.wavelet.impl.Gaus4PWavelet;
import main.wavelet.impl.MexicanHatWavelet;
import main.wavelet.impl.MorletWavelet;
import main.wavelet.transform.WaveletTransform;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by chrno on 10/18/14.
 */
public class MainForm extends JFrame {
    private JPanel rootPanel;
    private JPanel centerPanel = new JPanel();
    private JScrollPane scrollPane = new JScrollPane();
    private JScrollPane additionalScrollPane = new JScrollPane(
            JScrollPane.VERTICAL_SCROLLBAR_NEVER,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    private GraphicsPanel graphicsPanel = new GraphicsPanel();
    private GraphicsPanel graphicsPanel1;//панель для доп графика
    private SignalFile signalFile = new SignalFile();

    private JPanel buttonGroupPanel = new JPanel();
    private ButtonGroup buttonGroup = new ButtonGroup();

    public MainForm() {
        super();

//        setSize(400, 400);
        setContentPane(rootPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        pack();

        generateMenu();

        addTopPanel();
        addCenterPanel();
        addGraphicsPanel();

        setVisible(true);
    }

    private void addCenterPanel() {
        centerPanel.setLayout(new GridLayout(1, 1));
        centerPanel.setBackground(Color.PINK);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void addTopPanel() {
        JRadioButton jRadioButtonHat = new JRadioButton("mhat");
        JRadioButton jRadioButtonMorlet = new JRadioButton("morlet");
        JRadioButton jRadioButtonGaus3P = new JRadioButton("gaus3");
        JRadioButton jRadioButtonGaus4P = new JRadioButton("gaus4");
        buttonGroup.add(jRadioButtonHat);
        buttonGroup.add(jRadioButtonMorlet);
        buttonGroup.add(jRadioButtonGaus3P);
        buttonGroup.add(jRadioButtonGaus4P);
        buttonGroupPanel.add(jRadioButtonHat);
        buttonGroupPanel.add(jRadioButtonMorlet);
//        buttonGroupPanel.add(jRadioButtonGaus3P);
        buttonGroupPanel.add(jRadioButtonGaus4P);
        rootPanel.add(buttonGroupPanel, BorderLayout.NORTH);

        jRadioButtonHat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateWavelet(new MexicanHatWavelet());
            }
        });

        jRadioButtonMorlet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateWavelet(new MorletWavelet());
            }
        });

        jRadioButtonGaus3P.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateWavelet(new Gaus3PWavelet());
            }
        });

        jRadioButtonGaus4P.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateWavelet(new Gaus4PWavelet());
            }
        });

        buttonGroupPanel.setVisible(false);
    }

    private void addGraphicsPanel() {
        scrollPane = new JScrollPane(
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        graphicsPanel.setBackground(Color.BLACK);

        scrollPane.setViewportView(graphicsPanel);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void updateGraphicsPanel(GraphicsPanel panel, int width, int height) {
        panel.setPreferredSize(new Dimension(width, height));
        panel.revalidate();
    }

    private void addAdditionalGraphicsPanel() {
        if (centerPanel.getComponentCount() == 1) {
            centerPanel.setLayout(new GridLayout(2, 1));

            graphicsPanel1 = new GraphicsPanel();

            graphicsPanel1.setBackground(Color.BLACK);
            additionalScrollPane.setViewportView(graphicsPanel1);

            centerPanel.add(additionalScrollPane);
            centerPanel.revalidate();
        } else {
            centerPanel.remove(additionalScrollPane);
            centerPanel.setLayout(new GridLayout(1, 1));
            centerPanel.revalidate();
        }
    }

    private void renderSignal() {
        ArrayList<Float> signals = signalFile.getSignals();

        //исходная высота панели
//        int initialPanelHeight = panel.getHeight();
        int initialPanelHeight = scrollPane.getHeight();

        int scrollBarHeight = 24;
        float newPanelHeight = Math.abs(signalFile.getMaxValue()) + Math.abs(signalFile.getMinValue());

        // коэффициент для масштабирования графика
        float factor = (initialPanelHeight - scrollBarHeight) / newPanelHeight;
//        float factor = 1;

        newPanelHeight = newPanelHeight * factor;

        float timeInIteration = (float)signalFile.getTotalTimeToReceive() / (float)signalFile.getDataSize() *  (float)4086;
        int newPanelWidth = Math.round(signals.size() * timeInIteration);

        updateGraphicsPanel(graphicsPanel, newPanelWidth, (int) Math.ceil(newPanelHeight));

        graphicsPanel.setMaxValue(signalFile.getMaxValue());
        graphicsPanel.setMinValue(signalFile.getMinValue());
        graphicsPanel.setSignalFile(signalFile);
        graphicsPanel.repaint();
    }

    private void calculateWavelet(Wavelet wavelet) {
//        double Nmhat = Math.round(1.816 * ( signalFile.getCutOffFrequency() / (double)4096 ));
        WaveletTransform waveletTransform = new WaveletTransform(wavelet);
//        WaveletTransform waveletTransform = new WaveletTransform(new Gaus3PWavelet());
//        WaveletTransform waveletTransform = new WaveletTransform(new Gaus4PWavelet());

        float minValue = signalFile.getMinValue();
        float maxValue = signalFile.getMaxValue();

        ArrayList<Float> values = new ArrayList<>();
        for (int m = 0; m < 1000; m++) {
            double value = waveletTransform.calculateTransform(m, signalFile.getSignals());
            if (value < minValue) {
                minValue = (float)value;
            }
            if (value > maxValue) {
                maxValue = (float)value;
            }

            values.add((float)value);
        }
//        double[] csignals = new double[1000];
//        for (int i = 0; i < 1000; i++) {
//            csignals[i] = (double)signalFile.getSignals().get(i);
//        }
//
////        CWT cwt = new CWT(2, 1, 1.816);
//        CWT cwt = new CWT(2, 1, 2);
//
//        Complex[] res = cwt.complexTransform(csignals);
//
//        ArrayList<Float> values = new ArrayList<>();
//        for (int m = 0; m < 1000; m++) {
//
//            values.add((float)res[m].real());
////            System.out.println(value);
//        }
        graphicsPanel.setMaxValue(maxValue);
        graphicsPanel.setMinValue(minValue);
        graphicsPanel.setWaveLetValues(values);
        graphicsPanel.repaint();
    }

    private void generateMenu() {

        JMenuBar menuBar = new JMenuBar();

        JMenu menuFile = new JMenu("File");

        JMenuItem itemOpen = new JMenuItem("Open");
        JCheckBoxMenuItem itemAdditionalPanel = new JCheckBoxMenuItem("Show Additional Panel");
        JMenuItem itemInfo = new JMenuItem("file info");

        menuFile.add(itemOpen);
//        menuFile.add(itemAdditionalPanel);
        menuFile.add(itemInfo);

        menuBar.add(menuFile);

        setJMenuBar(menuBar);

        itemOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser saveFile = new JFileChooser();
                saveFile.showOpenDialog(null);

                File file = saveFile.getSelectedFile();

                try {
//                    System.out.print(file.getPath());
                    signalFile = BufferFileReader.readFile(file);

                    System.out.println(signalFile.getSignature());
                    System.out.println(signalFile.getChannelsCount());
                    System.out.println(signalFile.getPointsCountAtTheSameTime());
                    System.out.println(signalFile.getSpectralLinesCount());
                    System.out.println(signalFile.getCutOffFrequency());
                    System.out.println(signalFile.getFrequencyResolution());
                    System.out.println(signalFile.getTimeToReceive());
                    System.out.println(signalFile.getUserBlocksCount());
                    System.out.println(signalFile.getDataSize());
                    System.out.println(signalFile.getSystemBlocksCount());
                    System.out.println(signalFile.getMaxValue());
                    System.out.println(signalFile.getMinValue());

                    // чистим лайны
                    graphicsPanel.setLines(new ArrayList<Line>());
                    graphicsPanel.setWaveletLines(new ArrayList<Line>());
                    graphicsPanel.setWaveLetValues(new ArrayList<Float>());

                    renderSignal();
                    buttonGroupPanel.setVisible(true);
                    buttonGroup.clearSelection();
                } catch (NullPointerException | IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        itemAdditionalPanel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addAdditionalGraphicsPanel();
            }
        });

        itemInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (signalFile.getDataSize() > 0) {
                    JOptionPane.showMessageDialog(null, signalFile.toString());
                } else {
                    JOptionPane.showMessageDialog(null, "Файл не открыт");
                }
            }
        });
    }
}
