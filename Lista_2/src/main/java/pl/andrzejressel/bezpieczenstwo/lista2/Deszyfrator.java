package pl.andrzejressel.bezpieczenstwo.lista2;

import akka.actor.UntypedActor;
import pl.andrzejressel.bezpieczenstwo.lista2.helpers.RC4Helper;
import pl.andrzejressel.bezpieczenstwo.lista2.kodowanie.ASCII;

public class Deszyfrator extends UntypedActor {

    private ASCII ascii;

    public Deszyfrator(ASCII ascii) {
        this.ascii = ascii;
    }

    @Override
    public void onReceive(Object message) throws Exception {

        Message wiadomosc = (Message) message;

        String wynik = RC4Helper.deszyfruj(wiadomosc.kodDoDeszyfracji, wiadomosc.wiadomosc);

        boolean ok = true;

        for (char charr : wynik.toCharArray()) {

            if (!ascii.whiteList.contains((int) charr)) {
                ok = false;
                break;
            }

        }

        if (ok) {
            System.out.println("WYNIK: " + wynik + " Klucz: " + wiadomosc.kodDoDeszyfracji);
        }

        Zadanie2.atomicInteger.incrementAndGet();

    }
}
