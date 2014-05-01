package crypto;

import sun.text.normalizer.UTF16;

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;

import static javax.xml.bind.DatatypeConverter.parseHexBinary;
import static javax.xml.bind.DatatypeConverter.printByte;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

/**
 * Created by mh on 30/04/14.
 */
public class CryptoTestClient {

    // create a string to encrypt

    // ask for keys

    // encrypt string

    // decrypt string

    public static void main(String[] args) {

        Crypto crypto = new Crypto();

        String plaintext = "Hello! 012345000 ;-) &auml; <tag> </tag>";

        String algorithm = Crypto.AES;

        SecretKey key = crypto.getKey(algorithm);


        // save and read back key (file will be a hexadecimal string)
        crypto.saveKeyToFile(key, "testkey.jmk");
        SecretKey key2 = crypto.readKeyFromFile("testkey.jmk", algorithm);

        // use the key we saved to file
        String encyptedHex = crypto.encrypt(plaintext,key2,algorithm);
        String decrypted = crypto.decrypt(encyptedHex,key2,algorithm);

        // we can use conversion methods directly too
        String keyHex = crypto.keyToHex(key);
        SecretKey backToKey = crypto.hexToKey(keyHex,algorithm);


        System.out.println("--------------------------");
        System.out.println("Plaintext to encrypt:");
        System.out.println(plaintext);
        System.out.println("Key saved to file:");
        System.out.println(key.toString());
        System.out.println("Key in hexadecimal:");
        System.out.println(keyHex);
        System.out.println("Key read from file:");
        System.out.println(key2.toString());
        System.out.println("Encrypted text as hexadecimal:");
        System.out.println(encyptedHex);
        System.out.println("Decrypted plaintext:");
        System.out.println(decrypted);
        System.out.println("--------------------------");

    }
}

//String a = "Hello!";
//System.out.println(a);
//
//        byte[] abytes;
//
//        try {
//
//        abytes = a.getBytes("UTF-8");   // string to bytes (then encrypt)
//
//        } catch (UnsupportedEncodingException e) {
//        e.printStackTrace();
//        return;
//        }
//
//
//
//        String ahex = printHexBinary(abytes);   // bytes to hexstring (then send as message)
//
//        System.out.println(ahex);
//
//        byte[] bbytes = parseHexBinary(ahex);   // hexstring to bytes
//
//        String b;
//
//        try {
//
//        b = new String(bbytes, "UTF-8");
//
//        } catch (UnsupportedEncodingException e) {
//        e.printStackTrace();
//        return;
//        }
//
//        System.out.println(b);
