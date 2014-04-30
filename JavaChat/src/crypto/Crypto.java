package crypto;

import javax.crypto.Cipher;
import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.util.HashMap;

import static javax.xml.bind.DatatypeConverter.parseHexBinary;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

/**
 * Created by mh on 30/04/14.
 */
public class Crypto {

    // get key from Keys

    Keys keys;

    // supports several algorithms,
    // we have to make a custom caesar cipher so this may not be the smartest solution
    HashMap<String,Cipher> ciphers;

    void createCipher(){
        try {
            ciphers.put("AES", Cipher.getInstance("AES/CBC/NoPadding"));
        } catch (Exception e) {
            System.err.println("Crypto couldn't get Cipher: " + e.toString());
        }

//        try {
//            ciphers.put("RSA", Cipher.getInstance("AES/CBC/NoPadding"));
//        } catch (Exception e) {
//            System.err.println("Crypto couldn't get Cipher: " + e.toString());
//        }
    }

    /**
     * Encrypts a message
     * @param message as string
     * @param key private key
     * @return hexadecimal string
     */
    String encrypt(String message, PrivateKey key, String algorithm) {

        Cipher cipher = ciphers.get(algorithm);

        // convert to bytes
        byte[] messageBytes;
        try {

            messageBytes = message.getBytes("UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        // encrypt
        byte[] messageBytesEncrypted;
        try {

            cipher.init(Cipher.ENCRYPT_MODE, key);
            messageBytesEncrypted = cipher.doFinal(messageBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // convert to hexstring
        return printHexBinary(messageBytesEncrypted);
    }

    /**
     * Decrypts hex encoded string
     * @param encryptedHex
     * @param key
     * @return
     */
    String decrypt(String encryptedHex, PrivateKey key, String algorithm) {

        Cipher cipher = ciphers.get(algorithm);

        byte[] encryptedBytes = parseHexBinary(encryptedHex);   // hexstring to bytes

        // decrypt
        byte[] decryptedBytes;
        try {

            cipher.init(Cipher.DECRYPT_MODE, key);
            decryptedBytes = cipher.doFinal(encryptedBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // convert to string message
        try {
            return new String(decryptedBytes, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    // add a field to SocketThread with encryption info
}
