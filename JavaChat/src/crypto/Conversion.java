package crypto;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.math.BigInteger;

import static javax.xml.bind.DatatypeConverter.parseHexBinary;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

/**
 * Created by mh on 04/05/14.
 */
public class Conversion {

    public static synchronized SecretKey hexToKey(String keyHex, String algorithm) {

        byte[] keyBytes = parseHexBinary(keyHex);
        return new SecretKeySpec(keyBytes, algorithm);
    }

    public static synchronized String keyToHex(SecretKey key) {

        byte[] keyBytes = key.getEncoded();
        return printHexBinary(keyBytes);
    }

    public static synchronized int hexToInt(String hexString) {

        BigInteger bi = new BigInteger(hexString,16);
        return bi.intValue();
    }

    public static synchronized String intToHex(int i) {
        return Integer.toHexString(i).toUpperCase();
    }
}
