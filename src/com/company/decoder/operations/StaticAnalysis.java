package com.company.decoder.operations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaticAnalysis extends AbstractRotator {
    private final int LETTERS_IN_ALPHABET = Math.max(UPPERCASE_ENG.size(), UPPERCASE_RU.size());
    private final int PERCENT = 60;

    public StaticAnalysis(String fileName, String fileForStaticAnalysis) {
        this.fileName = fileName;
        this.fileForStaticAnalysis = fileForStaticAnalysis;
    }

    public String analyze() {
        key = getKey();
        if (key == 0) {
            String message = "Failed to decode. Please try another files";
            System.out.println(message);
            return message;
        } else {
            String outputFileName = generateFileName(STATIC_ANALYSIS);
            rotateAlphabet(key);
            write(outputFileName);
        }
        return null;
    }

    /* the following method accepts list of words and returns a list of the most repeated words.
   Max length of the returned list is 30 */
    private static List<String> getMostRepeatedWords(List<String> lines) {
        List<String> repeated = new ArrayList<>();
        /* counting the words' number */
        Map<String, Integer> map = new HashMap<>();
        for (String line : lines) {
            String[] word = line.split(" ");
            for (int i = 0; i < word.length; i++) {
                word[i] = word[i].replaceAll("[^A-Za-zА-ЯЁа-яё]", "").toLowerCase();
                if (map.containsKey(word[i])) {
                    map.put(word[i], map.get(word[i]) + 1);
                } else {
                    map.put(word[i], 1);
                }
            }
        }

        /* selecting 30 most repeated words */
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (repeated.size() == 30) {
                break;
            }
            if (!repeated.contains(entry.getKey()) && entry.getKey().length() > 1) {
                String tempS = entry.getKey();
                int tempI = entry.getValue();
                for (Map.Entry<String, Integer> entry2 : map.entrySet()) {
                    if (!entry2.getKey().equals(tempS) && entry2.getKey().length() > 1
                            && !repeated.contains(entry2.getKey()) && tempI < entry2.getValue()) {
                        tempS = entry2.getKey();
                        tempI = entry2.getValue();
                    }
                }
                repeated.add(tempS);
            }
        }

        return repeated;
    }

    /* the following method reads all lines from the encoded file and from the file for analysis and gets the most
    repeated words from each file. Then takes the words with the same length and starts trying keys from 1 to LETTERS_IN_ALPHABET
    inclusively. If at the certain key the words are equal, the key is checked if it's acceptable for the other words
    and returns it if so. Returns 0 if no key was found. */
    private int getKey() {
        int localKey = 0;
        try {
            List<String> encoded = getMostRepeatedWords(Files.readAllLines(Path.of(fileName)));
            List<String> forAnalysis = getMostRepeatedWords(Files.readAllLines(Path.of(fileForStaticAnalysis)));

            for (int i = 0; i < encoded.size(); i++) {
                for (int j = 0; j < forAnalysis.size(); j++) {
                    if (encoded.get(i).length() == forAnalysis.get(j).length()) {
                        String word = forAnalysis.get(j);
                        char[] chars = encoded.get(i).toCharArray();
                        for (int k = 1; k <= LETTERS_IN_ALPHABET; k++) {
                            rotateAlphabet(-k);
                            if (word.equals(String.valueOf(replaceChars(chars)))) {
                                if (isKeyAcceptable(encoded, forAnalysis)) {
                                    return -k;
                                }
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return localKey;
    }

    /* the following method checks if the key is acceptable. It decodes all encoded words from the list and counts
    how many of them match the words from the list for analysis. Then it counts the percentage of the matched words from
    total amount and if it's 'PERCENT' or more the key is considered as acceptable (returns true) */
    private boolean isKeyAcceptable(List<String> encoded, List<String> forAnalysis) {
        int count = 0;
        for (String word : encoded) {
            if (forAnalysis.contains(String.valueOf(replaceChars(word.toCharArray())))) {
                count++;
            }
        }

        if (count * 1d / forAnalysis.size() * 100 >= PERCENT) {
            return true;
        }

        return false;
    }
}