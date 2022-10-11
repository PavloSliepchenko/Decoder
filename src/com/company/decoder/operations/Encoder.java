package com.company.decoder.operations;

public class Encoder extends AbstractRotator {
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