package com.company.decoder;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class GUI extends UtilityClass {
    private JPanel panel;
    private JFrame frame;
    private JButton encode;
    private JButton decode;
    private JButton staticAnalysis;
    private JFileChooser chooser;

    public GUI() {
        initialize();
    }

    private void initialize() {
        panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(2, 3, 2, 3));

        JPanel layout = new JPanel(new GridBagLayout());
        layout.setBorder(new EmptyBorder(5, 5, 5, 5));
        JPanel btnPanel = new JPanel(new GridLayout(10, 1, 10, 5));
        JLabel label = new JLabel("Please choose one of the following options:");
        btnPanel.add(label);

        encode = new JButton("Encode");
        decode = new JButton("Decode");
        staticAnalysis = new JButton("Static analysis");

        encode.addActionListener(new Encode());
        decode.addActionListener(new Decode());
        staticAnalysis.addActionListener(new StaticAnalys());

        btnPanel.add(encode);
        btnPanel.add(decode);
        btnPanel.add(staticAnalysis);

        layout.add(btnPanel);

        panel.add(layout, BorderLayout.CENTER);

        frame = new JFrame("Caesar decoder");
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.setSize(500, 300);
        frame.setVisible(true);

    }

    private class Encode implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            frame.dispose();
            String afile = getAfile(ENCODE);
            if (afile != null) {

                fileName = afile;
                key = getKey();

                Encoder encoder = new Encoder(fileName, key);
                encoder.encode();

                encodeSuccess();

            } else {
                canceled();
            }
        }
    }

    private class Decode implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.dispose();

            String afile = getAfile(DECODE);
            if (afile != null) {
                fileName = afile;
                key = getKey();

                Decoder decoder = new Decoder(fileName, key);
                decoder.decode();

                decodeSuccess();

            } else {
                canceled();
            }
        }
    }

    private class StaticAnalys implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            frame.dispose();
            String afile = getAfile(DECODE);
            if (afile != null) {
                fileName = afile;
                String fileForStatAnalysis = getAfile(STATIC_ANALYSIS);
                if (fileForStatAnalysis != null) {
                    fileForStaticAnalysis = fileForStatAnalysis;

                    StaticAnalysis analysis = new StaticAnalysis(fileName, fileForStaticAnalysis);
                    String result = analysis.analyze();

                    if (result != null) {
                        fail(result);
                    }
                    decodeSuccess();

                } else {
                    canceled();
                }
            } else {
                canceled();
            }

        }
    }

    private String getAfile(int mode) {
        chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        chooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter extFilter = new FileNameExtensionFilter("Text files"
                , "txt", "doc", "docx", "pdf");
        chooser.addChoosableFileFilter(extFilter);

        if (mode == ENCODE) {
            chooser.setDialogTitle("Choose a file for encoding");
        } else if (mode == DECODE) {
            chooser.setDialogTitle("Choose a file for decoding");
        } else if (mode == STATIC_ANALYSIS) {
            chooser.setDialogTitle("Choose a file for static analysis");
        }

        int returnValue = chooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            return selectedFile.getAbsolutePath();
        }
        return null;
    }

    public int getKey() {
        String localKey = JOptionPane.showInputDialog(null, "Enter a key please:",
                "key", JOptionPane.PLAIN_MESSAGE);

        if (localKey == null) {
            canceled();
        }

        if (!(localKey.matches("\\+{0,1}\\d+") || localKey.matches("\\-{0,1}\\d+"))) {
            JOptionPane.showMessageDialog(null, localKey + " - in not a number",
                    "Not a number", JOptionPane.ERROR_MESSAGE);
            exit();
        }
        return Integer.parseInt(localKey);
    }

    private void canceled() {
        JOptionPane.showMessageDialog(null, "An action was canceled",
                "Cancel", JOptionPane.CANCEL_OPTION);
        exit();
    }

    private void exit() {
        System.exit(0);
    }

    private void decodeSuccess() {
        JOptionPane.showMessageDialog(null, "The file was successfully decoded",
                "Success", JOptionPane.PLAIN_MESSAGE);
        exit();
    }

    private void encodeSuccess() {
        JOptionPane.showMessageDialog(null, "The file was successfully encoded",
                "Success", JOptionPane.PLAIN_MESSAGE);
        exit();
    }

    private void fail(String message) {
        JOptionPane.showMessageDialog(null, message,
                "Fail", JOptionPane.ERROR_MESSAGE);
        exit();
    }
}