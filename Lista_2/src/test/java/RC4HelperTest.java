import org.junit.Test;
import pl.andrzejressel.bezpieczenstwo.lista2.helpers.RC4Helper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RC4HelperTest {

    @Test
    public void test1() throws Exception {

        List<Byte> zaszyfrowane = new ArrayList<>();
        zaszyfrowane.add((byte) 5);
        zaszyfrowane.add((byte) 251);
        zaszyfrowane.add((byte) 105);
        zaszyfrowane.add((byte) 21);
        zaszyfrowane.add((byte) 116);
        zaszyfrowane.add((byte) 97);
        
        String klucz = "123456789";

        assertEquals(RC4Helper.deszyfruj(klucz, zaszyfrowane), "123456");

    }

}
