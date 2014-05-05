package crypto;

/**
 * Created by mh on 30/04/14.
 */
public class CryptoTestClient {


    public static void main(String[] args) {

        // first make a crypto object
        Crypto crypto = new Crypto();

        //String plaintext = "Hello! 012345000 ;-) &auml; <tag> </tag> tilde~";

        // we make a test string with ascii 32-126
        StringBuilder sb = new StringBuilder();
        for (int i = 32; i <= 126; i++) {
            sb.append((char) i);
        }
        String plaintext = sb.toString();

        // print list of supported algorithms
        System.out.println("Supported algorithms:");
        for(String e : crypto.getSupportedCryptoList()) {
            System.out.println(e);
        }

        // test a built in algorithm
        String algorithm = Crypto.AES;
        //String algorithm = Crypto.DES;
        //String algorithm = Crypto.BLOWFISH;


        // test twice
        for (int i = 0; i < 2; i++) {

            // get a key in hex format
            String key = crypto.getKey(algorithm);

            // encrypt with it
            String encyptedHex = crypto.encrypt(plaintext, key, algorithm);

            // save key (file will be a hexadecimal string)
            crypto.saveKeyToFile(key, "testkey.jmk");

            // read back with readHexKeyFromFile
            String key2 = crypto.readHexKeyFromFile("testkey.jmk", algorithm);

            // decrypt with the key we saved to file
            String decrypted = crypto.decrypt(encyptedHex, key2, algorithm);


            System.out.println("--------------------------");
            System.out.println("Plaintext to encrypt:");
            System.out.println(plaintext);
            System.out.println("Key in hexadecimal:");
            System.out.println(key);
            System.out.println("Key read from file:");
            System.out.println(key2);
            System.out.println("Encrypted text as hexadecimal:");
            System.out.println(encyptedHex);
            System.out.println("Decrypted plaintext:");
            System.out.println(decrypted);

            if (!plaintext.equals(decrypted)) {
                System.out.println("*** plaintext and decrypted are different !!! ***");
            }

            System.out.println("--------------------------");
        }

        // exercise caesar
        System.out.println("--------------------------");
        System.out.println("Exercising caesar!");
        System.out.println("Plaintext to encrypt:");
        System.out.println(plaintext);

        // set algorithm
        algorithm = Crypto.CAESAR;

        System.out.println("Testing keys 1-93.");
        for (int i = 1; i <= 93; i++) {

            String ckey = Conversion.intToHex(i);   // normally we would use getKey
            //System.out.println(ckey);

            String encryptedWithCaesar = crypto.encrypt(plaintext,ckey,algorithm);
            //System.out.println(encryptedWithCaesar); // amusing

            String decryptedWithCaesar = crypto.decrypt(encryptedWithCaesar,ckey,algorithm);

            if (!decryptedWithCaesar.equals(plaintext.toString())) {
                System.out.println("*** Decrypted string was different from encrypted! ***");
            }
        }

        System.out.println("Testing 100 random keys with large integers.");
        for (int i = 0; i < 100; i++) {
            String ckey = crypto.getKey(algorithm); // we get hex format right away
            // System.out.println(ckey);

            String encryptedWithCaesar = crypto.encrypt(plaintext,ckey,algorithm);
            //System.out.println(encryptedWithCaesar);

            String decryptedWithCaesar = crypto.decrypt(encryptedWithCaesar,ckey,algorithm);

            if (!decryptedWithCaesar.equals(plaintext.toString())) {
                System.out.println("*** Decrypted string was different from encrypted! ***");
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
