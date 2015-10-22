package pl.andrzejressel.bezpieczenstwo.lista2;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RoundRobinPool;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import pl.andrzejressel.bezpieczenstwo.lista2.interfaces.Zadanie;
import pl.andrzejressel.bezpieczenstwo.lista2.kodowanie.ASCII;
import pl.andrzejressel.bezpieczenstwo.lista2.kodowanie.SystemKodowania;

import java.net.URL;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Zadanie2 implements Zadanie {

    public static AtomicInteger atomicInteger = new AtomicInteger(0);
    private AtomicInteger atomicIntegerPrivate = new AtomicInteger(0);

    //Wiadomość: China Allegedly Arrested Hackers To Comply With The U.S. Government's Demands
    //Klucz: 449bdd83af52c65e

    @Override
    public void wykonaj(String indeks, SystemKodowania systemKodowania) throws Exception {

        ASCII ascii = new ASCII();

        String data = IOUtils.toString(new URL("http://zagorski.im.pwr.wroc.pl/courses/kbk2015/l1.php?id=" + indeks));

        String kryptogram = StringUtils.substringAfter(data, "a nastepnie zdeszyfruj kryptogram:");

        kryptogram = StringUtils.remove(kryptogram, "<br");
        kryptogram = StringUtils.remove(kryptogram, "/>");

        String czescKlucza = StringUtils.substringBetween(data, "znajdz klucz, ktorego 8 znakow to: <b>", ", a nastepnie zdeszyfruj kryptogram:");

        czescKlucza = StringUtils.remove(czescKlucza, "</");
        czescKlucza = StringUtils.remove(czescKlucza, "b>");

        long max = (long) Math.pow(16, 8);


        //TESTOWE_DANE
        //  czescKlucza = "aaaaaaaa";
        //  kryptogram = "01001001 00011000 11001001 00000010 11001010 01001001 10001110 00001101 01001010 11100000 11011101";
        //  max = 10000000;
        //TESTOWE_DANE


        List<Byte> test = new ArrayList<>();

        for (String char1 : StringUtils.split(kryptogram)) {
            test.add((byte) Integer.parseInt(char1, 2));
        }


        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        ActorSystem system = ActorSystem.create();

        int processors = Runtime.getRuntime().availableProcessors();

        ActorRef router = system.actorOf(Props.create(Deszyfrator.class, ascii).withRouter(new RoundRobinPool(processors * 20)));

        long i = 0;

        //Robimy po 10000000
        while (i != max) {

            atomicInteger.set(0);
            atomicIntegerPrivate.set(0);

            System.out.println("TEST " + i);

            for (int a = 0; a < 10000000 && i < max; a++) {

                String test2 = Long.toHexString(i);

                while (test2.length() < 8) {
                    test2 = "0" + test2;
                }

                String klucz = test2 + czescKlucza;

                router.tell(new Message(test, klucz), null);

                i++;

                atomicIntegerPrivate.incrementAndGet();

            }


            while (atomicInteger.get() != atomicIntegerPrivate.get()) {
                Thread.sleep(1);
            }

        }

        System.out.println("THE END");
        system.shutdown();
        System.exit(0);

    }


}
