package crypto;

import java.math.BigInteger;

/**
 * Created by mh on 04/05/14.
 */
public class ConversionTestClient {

    public static void main(String[] args) {

        int NEG = -12345;
        int MIN = Integer.MIN_VALUE;
        int MAX = Integer.MAX_VALUE;
        int POS = 12345;

        //BigInteger bi;
        int i;
        String hex;

        System.out.println("----------------------");
        System.out.println("Converting: " + POS);
        hex = Conversion.intToHex(POS);
        System.out.println("Hex: " + hex);
        i = Conversion.hexToInt(hex);
        System.out.println("Back: " + i);

        System.out.println("----------------------");
        System.out.println("Converting: " + MIN);
        hex = Conversion.intToHex(MIN);
        System.out.println("Hex: " + hex);
        i = Conversion.hexToInt(hex);
        System.out.println("Back: " + i);

        System.out.println("----------------------");
        System.out.println("Converting: " + MAX);
        hex = Conversion.intToHex(MAX);
        System.out.println("Hex: " + hex);
        i = Conversion.hexToInt(hex);
        System.out.println("Back: " + i);

        System.out.println("----------------------");
        System.out.println("Converting: " + NEG);
        hex = Conversion.intToHex(NEG);
        System.out.println("Hex: " + hex);
        i = Conversion.hexToInt(hex);
        System.out.println("Back: " + i);

    }
}




        /*
        System.out.println("----------------------");
        System.out.println("Converting: " + POS);
        hex = Integer.toHexString(POS).toUpperCase();
        System.out.println("Hex: " + hex);
        bi = new BigInteger(hex,16);
        i = bi.intValue();
        System.out.println("Back: " + i);

        System.out.println("----------------------");
        System.out.println("Converting: " + NEG);
        hex = Integer.toHexString(NEG).toUpperCase();
        System.out.println("Hex: " + hex);
        bi = new BigInteger(hex,16);
        i = bi.intValue();
        System.out.println("Back: " + i);

        System.out.println("----------------------");
        System.out.println("Converting: " + MIN);
        hex = Integer.toHexString(MIN).toUpperCase();
        System.out.println("Hex: " + hex);
        bi = new BigInteger(hex,16);
        i = bi.intValue();
        System.out.println("Back: " + i);

        System.out.println("----------------------");
        System.out.println("Converting: " + MAX);
        hex = Integer.toHexString(MAX).toUpperCase();
        System.out.println("Hex: " + hex);
        bi = new BigInteger(hex,16);
        i = bi.intValue();
        System.out.println("Back: " + i);
        */