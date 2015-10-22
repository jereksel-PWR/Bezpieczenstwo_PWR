package pl.andrzejressel.bezpieczenstwo.lista2.kodowanie;

import java.util.List;

public interface SystemKodowania {

    int getDlugosc();

    List<String> getAllCombinations();

    List<Integer> getWhitelist();

}
