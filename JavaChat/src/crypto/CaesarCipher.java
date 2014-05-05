package crypto;

import java.nio.charset.StandardCharsets;
import static javax.xml.bind.DatatypeConverter.parseHexBinary;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

/**
 * Created by mh on 03/05/14.
 */
public class CaesarCipher {

    public static final int ASCII_START = 32;
    public static final int ASCII_END = 126;
    public static final int ASCII_RANGE = ASCII_END - ASCII_START;

    public String encrypt(String message, int key) {

        int adder = (key % ASCII_RANGE-1) + 1;  // we add only a number between 1 and 93

        char[] messageArray = message.toCharArray();
        StringBuilder sb = new StringBuilder();

        for (char letter : messageArray) {

            int t = ((int) letter) + adder;
            if (t > ASCII_END) {
                t = (t % (ASCII_END + 1)) + ASCII_START;  // t mod 127
            }

            letter = (char) t;
            sb.append(letter);
        }

        return printHexBinary(sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    public String decrypt(String encryptedHex, int key) {

        int adder = (key % ASCII_RANGE-1) + 1;  // we add only a number between 1 and 93

        StringBuilder sb = new StringBuilder();

        byte[] encryptedBytes = parseHexBinary(encryptedHex);
        char[] encryptedChar = new String(encryptedBytes, StandardCharsets.UTF_8).toCharArray();

        for (char letter : encryptedChar) {
            int t = ((int) letter) - adder;

            if (t < ASCII_START) {
                t = ASCII_START-t;
                t = (ASCII_END + 1) - t;
            }

            letter = (char) t;
            sb.append(letter);
        }

        return sb.toString();
    }

}

//Random keyGen;  // not cryptosecure

//    public CaesarCipher() {
//        keyGen = new Random();
//    }

//    public int getKey() {
//
//        return keyGen.nextInt(Integer.MAX_VALUE-10)+1;  // between 1 and something very large
//        //return keyGen.nextInt(10)+1;  // between 1 and 10 for testing
//    }