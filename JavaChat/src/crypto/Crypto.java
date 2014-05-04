package crypto;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static javax.xml.bind.DatatypeConverter.parseHexBinary;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

/**
 * Created by mh on 01/05/14.
 */
public class Crypto  extends Ciphers {


    public synchronized void saveKeyToFile(SecretKey key, String filepath) {

        // convert key to hex string and write to file
        String keyHex = keyToHex(key);
        saveToFile(keyHex, filepath);
    }

    public synchronized void saveKeyToFile(String keyHex, String filepath) {

        // write directly to file
        saveToFile(keyHex, filepath);
    }

    private synchronized void saveToFile(String stringToSave, String filepath) {

        Path path = null;
        try {
            path = Paths.get(filepath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        BufferedWriter writer;
        try {
            writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
            writer.write(stringToSave);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized SecretKey readKeyFromFile(String filepath, String algorithm) {

        String keyHex = readFromFile(filepath);
        return hexToKey(keyHex, algorithm);
    }

    public synchronized String readHexKeyFromFile(String filepath, String algorithm) {
        return readFromFile(filepath);
    }

    private synchronized String readFromFile(String filepath) {

        Path path = null;
        try {
            path = Paths.get(filepath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        BufferedReader reader;
        String stringToRead = null;

        // read key (hex string) and convert to SecretKey
        try {

            reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
            stringToRead = reader.readLine(); // we assume key is one line
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringToRead;
    }

}


//        try {
//
//            keyFactories.put(Crypto.AES, KeyFactory.getInstance(Crypto.AES));
//            keyFactories.put(Crypto.DES, KeyFactory.getInstance(Crypto.DES));
//
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }

//HashMap<String,KeyFactory> keyFactories;

 //   public Crypto() {
//
//    }