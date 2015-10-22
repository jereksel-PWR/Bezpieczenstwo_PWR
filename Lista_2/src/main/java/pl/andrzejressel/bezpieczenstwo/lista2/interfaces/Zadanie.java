package pl.andrzejressel.bezpieczenstwo.lista2.interfaces;

import pl.andrzejressel.bezpieczenstwo.lista2.kodowanie.SystemKodowania;

public interface Zadanie {
    void wykonaj(String indeks, SystemKodowania systemKodowania) throws Exception;
}
