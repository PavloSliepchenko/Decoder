package com.company.decoder;

import com.company.decoder.GUI.GUI;
import com.company.decoder.operations.Decoder;
import com.company.decoder.operations.Encoder;
import com.company.decoder.operations.StaticAnalysis;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            new GUI();
        } else if (args.length == 3) {
            String operation = args[0];
            String filepath = args[1];
            if (operation.equalsIgnoreCase("encode")) {
                if (checkArguments(filepath, args[2])) {
                    int key = Integer.parseInt(args[2]);
                    Encoder encoder = new Encoder(filepath, key);
                    encoder.encode();
                }

            } else if (operation.equalsIgnoreCase("decode")) {
                if (checkArguments(filepath, args[2])) {
                    int key = Integer.parseInt(args[2]);
                    Decoder decoder = new Decoder(filepath, key);
                    decoder.decode();
                }

            } else if (operation.equalsIgnoreCase("bruteForce")
                    || operation.equalsIgnoreCase("staticAnalysis")) {
                if (Files.isRegularFile(Path.of(filepath)) && Files.isRegularFile(Path.of(args[2]))) {
                    StaticAnalysis analysis = new StaticAnalysis(filepath, args[2]);
                    analysis.analyze();
                } else {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    private static boolean checkArguments(String s1, String s2) {
        if (!Files.isRegularFile(Path.of(s1))
                || !(s2.matches("\\+{0,1}\\d+") || s2.matches("\\-{0,1}\\d+"))) {
            throw new IllegalArgumentException();
        }
        return true;
    }
}