package crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.HashMap;

import static javax.xml.bind.DatatypeConverter.parseHexBinary;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

/**
 * Created by mh on 30/04/14.
 */
public class Ciphers {

    // supports several algorithms,
    HashMap<String,Cipher> ciphers = new HashMap<String,Cipher>();
    CaesarCipher caesar = new CaesarCipher();

    public Ciphers() {
        createCiphers();
    }

    private synchronized void createCiphers(){
        try {

            ciphers.put(Crypto.AES, Cipher.getInstance(Crypto.AES));
            ciphers.put(Crypto.DES, Cipher.getInstance(Crypto.DES));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Encrypts a message with either cipher or caesar
     * @param message as string
     * @param hexKey key as hex string, is converted appropriately
     * @return hexadecimal string
     */
    public synchronized String encrypt(String message, String hexKey, String algorithm) {

        if (algorithm.equals("Caesar")) {
            int shift = Conversion.hexToInt(hexKey);
            return caesar.encrypt(message,shift);
        } else {
            SecretKey key = Conversion.hexToKey(hexKey, algorithm);
            return encrypt(message, key, algorithm);
        }
    }

    /**
     * Encrypts with one of the cipher objects.
     * @param message hex encoded
     * @param key SecretKey
     * @param algorithm
     * @return
     */
    private synchronized String encrypt(String message, SecretKey key, String algorithm) {

        Cipher cipher = ciphers.get(algorithm);

        byte[] messageBytes;
        byte[] messageBytesEncrypted;

        try {

            // convert to bytes
            messageBytes = message.getBytes(StandardCharsets.UTF_8);

            // encrypt
            cipher.init(Cipher.ENCRYPT_MODE, key);
            messageBytesEncrypted = cipher.doFinal(messageBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // convert to hexstring
        return printHexBinary(messageBytesEncrypted);
    }

    public synchronized String decrypt(String encryptedHex, String hexKey, String algorithm) {
        if (algorithm.equals("Caesar")) {
            int shift = Conversion.hexToInt(hexKey);
            return caesar.decrypt(encryptedHex,shift);
        } else {
            SecretKey key = Conversion.hexToKey(hexKey, algorithm);
            return decrypt(encryptedHex, key, algorithm);
        }
    }

    /**
     * Decrypts with cipher objects
     * @param encryptedHex hex encoded string
     * @param key SecretKey
     * @return decrypted string in human language
     */
    private synchronized String decrypt(String encryptedHex, SecretKey key, String algorithm) {

        Cipher cipher = ciphers.get(algorithm);

        // hexstring to bytes
        byte[] encryptedBytes = parseHexBinary(encryptedHex);

        byte[] decryptedBytes;
        String stringOut;
        try {

            // decrypt
            cipher.init(Cipher.DECRYPT_MODE, key);
            decryptedBytes = cipher.doFinal(encryptedBytes);

            // convert to string message
            stringOut = new String(decryptedBytes, StandardCharsets.UTF_8);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return stringOut;
    }

}

//        Cipher cipher = ciphers.get(algorithm);
//
//        byte[] messageBytes;
//        byte[] messageBytesEncrypted;
//
//        try {
//
//            // convert to bytes
//            messageBytes = message.getBytes(StandardCharsets.UTF_8);
//
//            // encrypt
//            cipher.init(Cipher.ENCRYPT_MODE, key);
//            messageBytesEncrypted = cipher.doFinal(messageBytes);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//
//        // convert to hexstring
//        return printHexBinary(messageBytesEncrypted);

/*
    public synchronized SecretKey hexToKey(String keyHex, String algorithm) {

        byte[] keyBytes = parseHexBinary(keyHex);
        return new SecretKeySpec(keyBytes, algorithm);
    }

    public synchronized String keyToHex(SecretKey key) {

        byte[] keyBytes = key.getEncoded();
        return printHexBinary(keyBytes);
    }

    public synchronized int hexToInt(String hexString) {
        byte[] keyBytes = parseHexBinary(hexString);
        return 0;
    }

    public synchronized String intToHex(int i) {
        return null;
    }
    */

//public static final String AES_TRANSFORM = "AES/CBC/NoPadding";
//public static final String DES_TRANSFORM = "DES/CBC/NoPadding";