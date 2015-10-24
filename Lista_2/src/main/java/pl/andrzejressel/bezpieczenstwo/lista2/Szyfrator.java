package pl.andrzejressel.bezpieczenstwo.lista2;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.engines.RC4Engine;
import org.bouncycastle.crypto.engines.Salsa20Engine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import pl.andrzejressel.bezpieczenstwo.lista2.kodowanie.SystemKodowania;
import scala.xml.PrettyPrinter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Szyfrator {

    public static void main(String[] args) throws Exception {

       Byte bytea = Byte.valueOf("-1111111",2);


        szyfruj(new File(args[1]), args[0]);
    }

    public static void szyfruj(File plik, String algorytm_nazwa) throws Exception {

        StreamCipher algorytm;
        KeyParameter keyParam;

        switch (algorytm_nazwa) {
            case "salsa20":
                algorytm = new Salsa20Engine();
                keyParam = new KeyParameter(RandomUtils.nextBytes(32));
                ParametersWithIV parametersWithIV = new ParametersWithIV(keyParam, RandomUtils.nextBytes(8));
                algorytm.init(true, parametersWithIV);
                break;
            case "rc4":
                algorytm = new RC4Engine();
                keyParam = new KeyParameter(RandomUtils.nextBytes(32));
                algorytm.init(true, keyParam);
                break;
            case "xor":
                algorytm = new XorEngine();
                break;
            default:
                throw new IllegalArgumentException("Brak takiego algorytmu");

        }


        List<String> list = FileUtils.readLines(plik);

        //  for (String linijka : list) {
        //      System.out.println(Arrays.toString(linijka.getBytes()));
        //  }

        List<String> test = list.stream()
                //Usuwamy puste linie
                .filter(e -> e.length() > 0)
                .map(String::getBytes)
                .map(bytes -> {

                    byte[] cypheredText = new byte[bytes.length];

                    algorytm.processBytes(bytes, 0, bytes.length, cypheredText, 0);

                    return cypheredText;

                })
                .map(Szyfrator::byteArrayToBinaryString)
                .collect(Collectors.toList());


        for (String zaszyfrowane : test) {
            System.out.println(zaszyfrowane);
        }

    }


    private static String byteArrayToBinaryString(byte[] bytes) {

        String toReturn = "";

        for (byte bytea : bytes) {


            String byteString = Integer.toUnsignedString(Byte.toUnsignedInt(bytea), 2);



            // Byte.toString(bytea);

            while (byteString.length() < 8) {
                byteString = "0" + byteString;
            }

            toReturn += byteString + " ";

        }

        return toReturn;

    }


}

