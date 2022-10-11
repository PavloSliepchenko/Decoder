package com.company.decoder.operations;

import com.company.decoder.utility.Alphabet;
import java.io.*;
import java.util.*;

public abstract class AbstractRotator implements Alphabet {
    protected final int ENCODE = 1;
    protected final int DECODE = 2;
    protected final int STATIC_ANALYSIS = 3;
    private String rotatedUppercaseEng;
    private String rotatedLowercaseEng;
    private String rotatedUppercaseRu;
    String rotatedLowercaseRu;
    protected String fileName;
    protected String fileForStaticAnalysis;
    protected int key;

    public void write(String outputFileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {

            while (reader.ready()) {
                String line = reader.readLine();
                char[] chars = line.toCharArray();
                chars = replaceChars(chars);
                writer.write(chars);
                writer.write(System.lineSeparator());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* the following method initializes fields of the class with the rotated at 'key' distance alphabets */
    public void rotateAlphabet(int key) {
        rotatedUppercaseEng = rotate(UPPERCASE_ENG, key);
        rotatedLowercaseEng = rotate(LOWERCASE_ENG, key);
        rotatedUppercaseRu = rotate(UPPERCASE_RU, key);
        rotatedLowercaseRu = rotate(LOWERCASE_RU, key);
    }

    /* the following method rotates the list at the 'key' distance and returns String without brackets or commas */
    private String rotate(List<String> original, int key) {
        List<String> copy = new ArrayList(original);
        Collections.rotate(copy, -key);
        return copy.toString().replaceAll("[^A-Za-zА-ЯЁа-яё]", "");
    }


    /* the following method takes a position of the character in the original alphabet and replaces it with the character
    from the rotated alphabet at the same position. Works with English and Russian alphabets, with uppercase and
    lowercase letters. Returns an array of characters */
    public char[] replaceChars(char[] chars) {
        char[] tempChars = Arrays.copyOf(chars, chars.length);
        for (int i = 0; i < tempChars.length; i++) {
            String tempString = String.valueOf(tempChars[i]);
            if (UPPERCASE_ENG.contains(tempString)) {
                int position = UPPERCASE_ENG.indexOf(tempString);
                tempChars[i] = rotatedUppercaseEng.charAt(position);
            } else if (LOWERCASE_ENG.contains(tempString)) {
                int position = LOWERCASE_ENG.indexOf(tempString);
                tempChars[i] = rotatedLowercaseEng.charAt(position);
            } else if (UPPERCASE_RU.contains(tempString)) {
                int position = UPPERCASE_RU.indexOf(tempString);
                tempChars[i] = rotatedUppercaseRu.charAt(position);
            } else if (LOWERCASE_RU.contains(tempString)) {
                int position = LOWERCASE_RU.indexOf(tempString);
                tempChars[i] = rotatedLowercaseRu.charAt(position);
            }
        }
        return tempChars;
    }

    public String generateFileName(int mode) {
        if (mode == ENCODE) {
            return fileName.substring(0, fileName.lastIndexOf('.'))
                    + "_encoded"
                    + fileName.substring(fileName.lastIndexOf('.'));
        } else if (mode == DECODE) {
            return fileName.substring(0, fileName.lastIndexOf('.'))
                    + "_decoded"
                    + fileName.substring(fileName.lastIndexOf('.'));
        } else {
            return fileName.substring(0, fileName.lastIndexOf('.'))
                    + "_decoded_" + "key-" + Math.abs(key)
                    + fileName.substring(fileName.lastIndexOf('.'));
        }
    }
}
