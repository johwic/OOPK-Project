package crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.HashMap;

import static javax.xml.bind.DatatypeConverter.parseHexBinary;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

/**
 * Created by mh on 30/04/14.
 */
public class Ciphers extends Keys {

    //public static final String AES_TRANSFORM = "AES/CBC/NoPadding";
    //public static final String DES_TRANSFORM = "DES/CBC/NoPadding";

    // supports several algorithms,
    // (we have to make a custom caesar cipher)
    HashMap<String,Cipher> ciphers = new HashMap<String,Cipher>();

    public Ciphers() {
        createCiphers();
    }

    synchronized void createCiphers(){
        try {

            ciphers.put(AES, Cipher.getInstance(AES));
            ciphers.put(DES, Cipher.getInstance(DES));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Encrypts a message
     * @param message as string
     * @param key private key
     * @return hexadecimal string
     */
    public synchronized String encrypt(String message, SecretKey key, String algorithm) {

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

    /**
     * Decrypts hex encoded string
     * @param encryptedHex string
     * @param key private key
     * @return decrypted string in human language
     */
    public synchronized String decrypt(String encryptedHex, SecretKey key, String algorithm) {

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

    // add a field to SocketThread with encryption info
}
