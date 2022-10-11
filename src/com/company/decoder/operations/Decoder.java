package com.company.decoder.operations;

public class Decoder extends AbstractRotator {
    public Decoder(String fileName, int key) {
        this.fileName = fileName;
        this.key = key;
    }

    public void decode() {
        String outputFileName = generateFileName(DECODE);
        rotateAlphabet(-key);
        write(outputFileName);
    }
}