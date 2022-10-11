package com.company.decoder;

public class Decoder extends UtilityClass{
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
