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


    public static void main(String[] args) {

        Crypto crypto = new Crypto();

        String plaintext = "Hello! 012345000 ;-) &auml; <tag> </tag> tilde~";

        String algorithm = Crypto.AES;
        //String algorithm = Crypto.DES;

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


        // exercise caesar
        System.out.println("Exercising caesar!");

        StringBuilder testString = new StringBuilder(); // we make a string with ascii 32-126
        for (int i = 32; i <= 126; i++) {
            testString.append((char) i);
        }

        System.out.println("Teststring: " + testString.toString());

        CaesarCipher caesar = new CaesarCipher();

        System.out.println("Testing keys 1-93.");
        for (int ckey = 1; ckey <= 93; ckey++) {
            String encryptedWithCaesar = caesar.encryptCaesar(testString.toString(),ckey);
            //System.out.println(encryptedWithCaesar); // amusing
            String decryptedWithCaesar = caesar.decryptCaesar(encryptedWithCaesar,ckey);

            if (!decryptedWithCaesar.equals(testString.toString())) {
                System.out.println("Decrypted string was different from encrypted!");
            }
        }

        System.out.println("Testing 100 random keys with large integers.");
        for (int i = 0; i < 100; i++) {
            int ckey = caesar.getCaesarKey();
            //System.out.println(ckey);
            String encryptedWithCaesar = caesar.encryptCaesar(testString.toString(),ckey);
            //System.out.println(encryptedWithCaesar);
            String decryptedWithCaesar = caesar.decryptCaesar(encryptedWithCaesar,ckey);

            if (!decryptedWithCaesar.equals(testString.toString())) {
                System.out.println("Decrypted string was different from encrypted!");
            }
        }

        System.out.println("Done exercising caesar.");


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
