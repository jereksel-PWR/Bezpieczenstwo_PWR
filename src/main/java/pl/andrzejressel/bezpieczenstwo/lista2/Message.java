package pl.andrzejressel.bezpieczenstwo.lista2;

import java.util.List;

public class Message {

    List<Byte> wiadomosc;
    String kodDoDeszyfracji;

    public Message(List<Byte> wiadomosc, String kodDoDeszyfracji) {
        this.wiadomosc = wiadomosc;
        this.kodDoDeszyfracji = kodDoDeszyfracji;
    }
}
