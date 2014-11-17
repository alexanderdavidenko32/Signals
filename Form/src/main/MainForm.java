package main;

import main.component.GraphicsPanel;
import main.file.SignalFile;
import main.file.reader.BufferFileReader;
import main.geometry.Line;
import main.spectrum.Spectrum;
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
    private GraphicsPanel spectrumPanel = new GraphicsPanel();//панель для Спектра
    private SignalFile signalFile = new SignalFile();
    private Spectrum spectrum;
    private ArrayList<Float> spectrumResults = new ArrayList<>();

    private JPanel buttonGroupPanel = new JPanel();
    private ButtonGroup buttonGroup = new ButtonGroup();

    /**
     * частота для которой определяется вейвлет
     * нужна для рассчета ширины вейвлета
     * (например 50 Гц)
     */
    private int Fw = 50;

    public MainForm() {
        super();

//        setSize(400, 400);
        setContentPane(rootPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
        final JCheckBox jCheckboxShowSourceSignal = new JCheckBox("Source Signal");
        final JCheckBox jCheckboxShowTransformedSignal = new JCheckBox("Transformed Signal");

        buttonGroup.add(jRadioButtonHat);
        buttonGroup.add(jRadioButtonMorlet);
        buttonGroup.add(jRadioButtonGaus3P);
        buttonGroup.add(jRadioButtonGaus4P);
        buttonGroupPanel.add(jRadioButtonHat);
        buttonGroupPanel.add(jRadioButtonMorlet);
        buttonGroupPanel.add(jRadioButtonGaus3P);
        buttonGroupPanel.add(jRadioButtonGaus4P);
        buttonGroupPanel.add(jCheckboxShowSourceSignal);
        buttonGroupPanel.add(jCheckboxShowTransformedSignal);
        rootPanel.add(buttonGroupPanel, BorderLayout.NORTH);

        jCheckboxShowSourceSignal.setSelected(true);
        jCheckboxShowTransformedSignal.setSelected(true);

        jRadioButtonHat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateWavelet(new MexicanHatWavelet( signalFile.getPointsCountAtTheSameTime(), Fw ));
            }
        });

        jRadioButtonMorlet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateWavelet(new MorletWavelet( signalFile.getPointsCountAtTheSameTime(), Fw ));
            }
        });

        jRadioButtonGaus3P.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateWavelet(new Gaus3PWavelet( signalFile.getPointsCountAtTheSameTime(), Fw ));
            }
        });

        jRadioButtonGaus4P.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateWavelet(new Gaus4PWavelet( signalFile.getPointsCountAtTheSameTime(), Fw ));
            }
        });

        jCheckboxShowSourceSignal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jCheckboxShowSourceSignal.isSelected()) {
                    graphicsPanel.setShowSignals(true);
                } else {
                    graphicsPanel.setShowSignals(false);
                }
                graphicsPanel.repaint();
            }
        });

        jCheckboxShowTransformedSignal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jCheckboxShowTransformedSignal.isSelected()) {
                    graphicsPanel.setShowWavelet(true);
                } else {
                    graphicsPanel.setShowWavelet(false);
                }
                graphicsPanel.repaint();
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

            spectrumPanel.setBackground(Color.BLACK);
            spectrumPanel.setPreferredSize(new Dimension(500, 160));
            additionalScrollPane.setViewportView(spectrumPanel);

            centerPanel.add(additionalScrollPane);
            centerPanel.revalidate();
            renderSpectrum();
        } else {
            centerPanel.remove(additionalScrollPane);
            centerPanel.setLayout(new GridLayout(1, 1));
            centerPanel.revalidate();
        }
    }

    private void renderSpectrum() {
        if (spectrumResults.size() > 0 && spectrumPanel.isVisible()) {
            ArrayList<Line> spectrumLines = new ArrayList<>();

            float newPanelHeight = Math.abs(spectrum.getMaxValue()) + Math.abs(spectrum.getMinValue());
            int scrollBarHeight = 1;
            int initialPanelHeight = (int)spectrumPanel.getPreferredSize().getHeight();

            float factor = (initialPanelHeight - scrollBarHeight) / newPanelHeight;
            float xAxis = spectrum.getMaxValue() * factor;

            for (int i = 0; i < spectrumResults.size() - 1; i++) {
                spectrumLines.add(new Line(i, xAxis, i, (spectrumResults.get(i) * factor) * -1 + xAxis));
            }

            spectrumPanel.setLines(spectrumLines);

            spectrumPanel.setMaxValue(spectrum.getMaxValue());
            spectrumPanel.setMinValue(spectrum.getMinValue());

            spectrumPanel.repaint();
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
        for (int m = 0; m < signalFile.getDataSize() - 1; m++) {
            double value = waveletTransform.calculateTransform(m, signalFile.getSignals());
            if (value < minValue) {
                minValue = (float)value;
            }
            if (value > maxValue) {
                maxValue = (float)value;
            }

            values.add((float)value);
        }
        graphicsPanel.setBaseWaveletValues(wavelet.getValues());
        graphicsPanel.setMaxValue(maxValue);
        graphicsPanel.setMinValue(minValue);
        graphicsPanel.setWaveLetValues(values);
        graphicsPanel.repaint();
    }

    private void generateMenu() {

        JMenuBar menuBar = new JMenuBar();

        JMenu menuFile = new JMenu("File");

        JMenuItem itemOpen = new JMenuItem("Open");
        JMenuItem itemСhangeWaveletFrequency = new JMenuItem("change Wavelet frequency");
        JCheckBoxMenuItem itemAdditionalPanel = new JCheckBoxMenuItem("Show spectrum");
        JMenuItem itemInfo = new JMenuItem("file info");

        menuFile.add(itemOpen);
        menuFile.add(itemСhangeWaveletFrequency);
        menuFile.add(itemAdditionalPanel);
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

                    System.out.println(signalFile.toString());

                    // чистим лайны
                    graphicsPanel.setLines(new ArrayList<Line>());
                    graphicsPanel.setWaveletLines(new ArrayList<Line>());
                    graphicsPanel.setWaveLetValues(new ArrayList<Float>());
                    graphicsPanel.setBaseWaveletValues(new ArrayList<Double>());
                    graphicsPanel.setBaseWaveletLines(new ArrayList<Line>());

                    renderSignal();
                    buttonGroupPanel.setVisible(true);
                    buttonGroup.clearSelection();

                    // Спектр
                    spectrum = new Spectrum(signalFile.getSignals());
                    spectrumResults = spectrum.calculateSpectrum();

                    renderSpectrum();

                } catch (NullPointerException | IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        itemСhangeWaveletFrequency.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String frequency = JOptionPane.showInputDialog("Введите частоту", Fw);

                if (frequency != null) {
                    Fw = Integer.parseInt(frequency);
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
