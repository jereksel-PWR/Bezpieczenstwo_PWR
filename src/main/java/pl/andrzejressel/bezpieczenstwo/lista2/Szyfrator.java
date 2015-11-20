package pl.andrzejressel.bezpieczenstwo.lista2;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.engines.RC4Engine;
import org.bouncycastle.crypto.engines.Salsa20Engine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class Szyfrator {

    String algorytm;
    byte[] key = RandomUtils.nextBytes(32);
    byte[] iv = RandomUtils.nextBytes(8);

    private Szyfrator(String algorytm) {

        String[] algorytmy = {"salsa20", "rc4", "xor"};

        if (!(ArrayUtils.contains(algorytmy, algorytm))) {
            throw new IllegalArgumentException("Brak takiego algorytmu");
        }

        this.algorytm = algorytm;

    }

    private StreamCipher getAlgorytm() {

        StreamCipher algorytm;
        KeyParameter keyParam;

        switch (this.algorytm) {
            case "salsa20":
                algorytm = new Salsa20Engine();
                keyParam = new KeyParameter(key);
                ParametersWithIV parametersWithIV = new ParametersWithIV(keyParam, iv);
                algorytm.init(true, parametersWithIV);
                break;
            case "rc4":
                algorytm = new RC4Engine();
                keyParam = new KeyParameter(key);
                algorytm.init(true, keyParam);
                break;
            case "xor":
                algorytm = new XorEngine();
                break;
            default:
                throw new IllegalArgumentException("Brak takiego algorytmu");

        }

        return algorytm;
    }

    public static void main(String[] args) throws Exception {

        //  Byte bytea = Byte.valueOf("-1111111", 2);

        new Szyfrator(args[0]).szyfruj(new File(args[1]));
    }

    private void szyfruj(File plik) throws Exception {

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

                    StreamCipher streamCipher = getAlgorytm();

                    streamCipher.processBytes(bytes, 0, bytes.length, cypheredText, 0);

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

