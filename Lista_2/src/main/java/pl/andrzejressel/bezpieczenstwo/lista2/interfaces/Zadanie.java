package pl.andrzejressel.bezpieczenstwo.lista2.interfaces;

import pl.andrzejressel.bezpieczenstwo.lista2.dane.ZrodloDanych;
import pl.andrzejressel.bezpieczenstwo.lista2.kodowanie.SystemKodowania;

public interface Zadanie {
    void wykonaj(ZrodloDanych zrodloDanych, SystemKodowania systemKodowania) throws Exception;
}
