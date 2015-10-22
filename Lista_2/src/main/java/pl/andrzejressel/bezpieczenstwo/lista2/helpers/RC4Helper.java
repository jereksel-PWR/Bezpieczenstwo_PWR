package pl.andrzejressel.bezpieczenstwo.lista2.helpers;

import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.engines.RC4Engine;
import org.bouncycastle.crypto.params.KeyParameter;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class RC4Helper {

    public static String deszyfruj(String klucz, List<Byte> data) {

        Byte[] bigZaszyfrowaneArray = new Byte[data.size()];

        data.toArray(bigZaszyfrowaneArray);

        byte[] zaszyfrowaneArray = ArrayUtils.toPrimitive(bigZaszyfrowaneArray);

        return deszyfruj(klucz, zaszyfrowaneArray);
    }

    public static String deszyfruj(String klucz, byte[] data) {

        try {

            StreamCipher rc4 = new RC4Engine();
            KeyParameter keyParam = new KeyParameter(klucz.getBytes(StandardCharsets.US_ASCII));

            rc4.init(false, keyParam);

            byte[] clearText = new byte[data.length];

            rc4.processBytes(data, 0, data.length, clearText, 0);

            return new String(clearText);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
