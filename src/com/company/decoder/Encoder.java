package com.company.decoder;

public class Encoder extends UtilityClass {
    public Encoder(String fileName, int key) {
        this.fileName = fileName;
        this.key = key;
    }

    public void encode() {
        String outputFileName = generateFileName(ENCODE);
        rotateAlphabet(key);
        write(outputFileName);
    }
}