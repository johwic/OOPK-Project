package crypto;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import static javax.xml.bind.DatatypeConverter.parseHexBinary;
import static javax.xml.bind.DatatypeConverter.printHexBinary;


/**
 * Created by mh on 30/04/14.
 */
public class Keys {

    public static final int AES_KEYLENGTH = 128;
    public static final int DES_KEYLENGTH = 56;

    public static final String AES = "AES";
    public static final String DES = "DES";

    // storage for generators and a handy list
    HashMap<String, KeyGenerator> keyGenerators = new HashMap<String, KeyGenerator>();
    HashMap<String, Integer> keyLengths = new HashMap<String, Integer>();
    LinkedList<String> supportedAlgorithms = new LinkedList<String>();

    public Keys() {

        // define key lengths for supported algorithms
        keyLengths.put(AES, AES_KEYLENGTH);
        keyLengths.put(DES, DES_KEYLENGTH);

        // create key generators
        createKeyGen(AES);
        createKeyGen(DES);

    }

    /**
     * Creates a key generator and stores in hashmap with
     * algorithm as key.
     *
     * @param algorithm string algorithm name
     */
    synchronized void createKeyGen(String algorithm) {

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

