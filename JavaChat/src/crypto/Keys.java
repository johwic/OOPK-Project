package crypto;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import static javax.xml.bind.DatatypeConverter.parseHexBinary;
import static javax.xml.bind.DatatypeConverter.printHexBinary;


/**
 * Created by mh on 30/04/14.
 */
public class Keys {

    // storage for generators and a handy list
    HashMap<String, KeyGenerator> keyGenerators = new HashMap<String, KeyGenerator>();
    HashMap<String, Integer> keyLengths = new HashMap<String, Integer>();

    Random caesarKeyGen = new Random();  // not cryptosecure


    LinkedList<String> supportedAlgorithms = new LinkedList<String>();

    public Keys() {

        // define key lengths for supported algorithms
        keyLengths.put(Crypto.AES, Crypto.AES_KEYLENGTH);
        keyLengths.put(Crypto.DES, Crypto.DES_KEYLENGTH);
        keyLengths.put(Crypto.BLOWFISH, Crypto.BLOWFISH_KEYLENGTH);


        // create key generators
        createKeyGen(Crypto.AES);
        createKeyGen(Crypto.DES);
        createKeyGen(Crypto.BLOWFISH);
    }

    /**
     * Creates a key generator and stores in hashmap with
     * algorithm as key.
     *
     * @param algorithm string algorithm name
     */
    private synchronized void createKeyGen(String algorithm) {

        try {
            KeyGenerator kg = KeyGenerator.getInstance(algorithm);
            kg.init(keyLengths.get(algorithm));

            keyGenerators.put(algorithm, kg);
            supportedAlgorithms.add(algorithm);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public synchronized SecretKey getKey(String algorithm) {

        return keyGenerators.get(algorithm).generateKey();
    }

    public synchronized int getCaesarKey() {

        return caesarKeyGen.nextInt(Integer.MAX_VALUE-10)+1;  // between 1 and something very large
    }

    // return a copy of supported algorithm list
    public synchronized LinkedList<String> getSupportedCryptoList() {
        LinkedList<String> out = new LinkedList<String>();
        for (String e : supportedAlgorithms) {
            out.addLast(e);
        }
        return out;
    }
}

//    void createKeyPairGen(String algorithm) {
//
//        try {
//            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
//            keyGen.initialize(128);
//        } catch (Exception e) {
//            System.err.println("Keys couldn't get KeyPairGenerator: " + e.toString());
//        }
//    }

//    void getKeyPair() {
//        keyPair = keyGen.generateKeyPair();
//
//        privateKey = keyPair.getPrivate();
//        publicKey = keyPair.getPublic();
//    }




    // convert keys to hex

    // convert keys from hex

    // store keys

