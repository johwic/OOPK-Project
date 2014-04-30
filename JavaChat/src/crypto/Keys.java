package crypto;

import javax.crypto.*;
import java.security.*;
import java.util.ArrayList;


/**
 * Created by mh on 30/04/14.
 */
public class Keys {

    KeyPairGenerator keyGen;

    KeyPair keyPair;
    PrivateKey privateKey;
    PublicKey publicKey;

    public void Keys() {
        // what we do here?
    }


    void createKeyPairGen(String algorithm) {

        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
            keyGen.initialize(128);
        } catch (Exception e) {
            System.err.println("Keys couldn't get KeyPairGenerator: " + e.toString());
        }
    }

    void getKeyPair() {
        keyPair = keyGen.generateKeyPair();

        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
    }




    // convert keys to hex

    // convert keys from hex

    // store keys
}
