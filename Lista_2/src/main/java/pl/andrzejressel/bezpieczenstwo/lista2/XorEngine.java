package pl.andrzejressel.bezpieczenstwo.lista2;

import org.apache.commons.lang3.RandomUtils;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.StreamCipher;

public class XorEngine implements StreamCipher {

    byte[] szyfr;

    public XorEngine() {
        szyfr = RandomUtils.nextBytes(8888);

    }

    @Override
    public void init(boolean forEncryption, CipherParameters params) throws IllegalArgumentException {

        szyfr = RandomUtils.nextBytes(8888);

    }

    @Override
    public String getAlgorithmName() {
        return null;
    }

    @Override
    public byte returnByte(byte in) {
        return 0;
    }

    @Override
    public int processBytes(byte[] in, int inOff, int len, byte[] out, int outOff) throws DataLengthException {

        for (int i = 0; i < len; i++) {

            out[i] = (byte) (in[i] ^ szyfr[i]);

        }

        return 0;

    }

    @Override
    public void reset() {

    }
}
