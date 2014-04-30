package crypto;

import sun.text.normalizer.UTF16;

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

        String a = "Hello!";
        System.out.println(a);

        byte[] abytes;

        try {

            abytes = a.getBytes("UTF-8");   // string to bytes (then encrypt)

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }



        String ahex = printHexBinary(abytes);   // bytes to hexstring (then send as message)

        System.out.println(ahex);

        byte[] bbytes = parseHexBinary(ahex);   // hexstring to bytes

        String b;

        try {

            b = new String(bbytes, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }

        System.out.println(b);


    }
}
