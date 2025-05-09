import l18n.Translations;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class Main extends JFrame {

    private JLabel inputLabel, outputLabel, statusLabel, qualityLabel, widthLabel;
    JTextField inputTextField;
    JTextField outputTextField;
    private JTextField widthTextField;
    private JButton inputButton, outputButton, compressButton;
    private JProgressBar progressBar;
    private JSlider qualitySlider;
    private int maxWidth = 1200;
    List<File> inputFiles;
    private String languages;

    public Main() {
        // Detect system language
        languages = System.getProperty("user.language");
        if (!Arrays.asList("de", "en", "vn").contains(languages)) {
            languages = "en";
        }

        setTitle(Translations.getLanguage(languages, "title"));
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        inputLabel = new JLabel(Translations.getLanguage(languages, "inputLabel"));
        outputLabel = new JLabel(Translations.getLanguage(languages, "outputLabel"));
        statusLabel = new JLabel(Translations.getLanguage(languages, "statusLabel"));
        qualityLabel = new JLabel(Translations.getLanguage(languages, "qualityLabel"));
        widthLabel = new JLabel(Translations.getLanguage(languages, "widthLabel"));
        inputTextField = new JTextField(30);
        outputTextField = new JTextField(30);
        widthTextField = new JTextField(10);
        widthTextField.setText(String.valueOf(maxWidth));
        inputButton = new JButton(Translations.getLanguage(languages, "inputButton"));
        outputButton = new JButton(Translations.getLanguage(languages, "outputButton"));
        compressButton = new JButton(Translations.getLanguage(languages, "compressButton"));
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        qualitySlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 75);
        qualitySlider.setMajorTickSpacing(25);
        qualitySlider.setPaintTicks(true);
        qualitySlider.setPaintLabels(true);


        inputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseInputFiles();
            }
        });

        outputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseOutputDirectory();
            }
        });

        compressButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                compressImages();
            }
        });


        gbc.gridx = 0;
        gbc.gridy = 0;
        add(inputLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(inputTextField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        add(inputButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(outputLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(outputTextField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        add(outputButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(qualityLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(qualitySlider, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(widthLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(widthTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(compressButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(progressBar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(statusLabel, gbc);

        setVisible(true);
    }

    void chooseInputFiles() {
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            // Verwende NSOpenPanel für macOS
            System.setProperty("apple.awt.fileDialog.useSystemExtensionAppearance", "true");
            FileDialog fileDialog = new FileDialog(this, Translations.getLanguage(languages,"chooseInputTitle"), FileDialog.LOAD);
            fileDialog.setMultipleMode(true);
            fileDialog.setVisible(true);

            String directory = fileDialog.getDirectory();
            File[] filenames = fileDialog.getFiles();

            if (directory != null && filenames != null) {
                inputFiles = new ArrayList<>();
                StringBuilder fileNamesString = new StringBuilder();

                for (File filename : filenames) {
                    File file = new File(directory, filename.getName());
                    inputFiles.add(file);
                    fileNamesString.append(filename).append(", ");
                }
                if (!fileNamesString.isEmpty()) {
                    fileNamesString.delete(fileNamesString.length() - 2, fileNamesString.length());
                }
                inputTextField.setText(fileNamesString.toString());
            }
        } else {
            // Use JFileChooser für other OS
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("File images", "jpg", "jpeg", "png", "gif"));
            fileChooser.setMultiSelectionEnabled(true);
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File[] selectedFiles = fileChooser.getSelectedFiles();
                inputFiles = Arrays.asList(selectedFiles);
                StringBuilder fileNames = new StringBuilder();
                for (File file : selectedFiles) {
                    fileNames.append(file.getName()).append(", ");
                }
                if (fileNames.length() > 0) {
                    fileNames.delete(fileNames.length() - 2, fileNames.length());
                }
                inputTextField.setText(fileNames.toString());
            }
        }
    }

     void chooseOutputDirectory() {
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            // Use NSOpenPanel for macOS in Directory-Mode
            System.setProperty("apple.awt.fileDialog.useSystemExtensionAppearance", "true");
            FileDialog fileDialog = new FileDialog(this, Translations.getLanguage(languages, "chooseOutputTitle"), FileDialog.SAVE);
            fileDialog.setMode(FileDialog.SAVE);
            fileDialog.setVisible(true);

            String directory = fileDialog.getDirectory();

            if (directory != null) {
                outputTextField.setText(directory);
            }
        } else {
            // Use JFileChooser for other OS
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedDirectory = fileChooser.getSelectedFile();
                outputTextField.setText(selectedDirectory.getAbsolutePath());
            }
        }
    }

    void compressImages() {
        if (inputFiles == null || inputFiles.isEmpty() || outputTextField.getText().isEmpty()) {
            statusLabel.setText(Translations.getLanguage(languages, "noInputOutput"));
            return;
        }

        File outputDirectory = new File(outputTextField.getText());
        if (!outputDirectory.exists()) {
            if (!outputDirectory.mkdirs()) {
                statusLabel.setText(Translations.getLanguage(languages, "outputDirError"));
                return;
            }
        }

        try {
            maxWidth = Integer.parseInt(widthTextField.getText());
        } catch (NumberFormatException e) {
            statusLabel.setText(Translations.getLanguage(languages, "invalidWidth"));
            return;
        }


        statusLabel.setText(Translations.getLanguage(languages, "compressionRunning"));
        progressBar.setValue(0);
        progressBar.setMaximum(inputFiles.size());
        AtomicInteger processedCount = new AtomicInteger(0);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (File inputImageFile : inputFiles) {
                    try {
                        BufferedImage originalImage = ImageIO.read(inputImageFile);
                        int originalWidth = originalImage.getWidth();
                        int originalHeight = originalImage.getHeight();
                        int newHeight = (int) (originalHeight * ((double) maxWidth / originalWidth));
                        BufferedImage scaledImage = new BufferedImage(maxWidth, newHeight, BufferedImage.TYPE_INT_RGB);
                        Graphics2D g = scaledImage.createGraphics();
                        g.drawImage(originalImage, 0, 0, maxWidth, newHeight, null);
                        g.dispose();

                        float quality = qualitySlider.getValue() / 100.0f;
                        String outputFileName = inputImageFile.getName();
                        File outputFile = new File(outputDirectory, outputFileName);

                        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
                        ImageWriteParam param = writer.getDefaultWriteParam();
                        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                        param.setCompressionQuality(quality);

                        FileImageOutputStream ios = new FileImageOutputStream(outputFile);
                        writer.setOutput(ios);
                        writer.write(null, new javax.imageio.IIOImage(scaledImage, null, null), param);
                        ios.close();
                        writer.dispose();

                        int currentCount = processedCount.incrementAndGet();
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                progressBar.setValue(currentCount);
                                statusLabel.setText("From: " + currentCount + " to " + inputFiles.size());
                                if (currentCount == inputFiles.size()) {
                                    statusLabel.setText(Translations.getLanguage(languages, "compressionFinished"));
                                }
                            }
                        });

                    } catch (IOException ex) {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                statusLabel.setText(Translations.getLanguage(languages, "fileError") + ex.getMessage());
                                progressBar.setValue(0);
                            }
                        });
                        return;
                    }
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main();
            }
        });
    }
}
