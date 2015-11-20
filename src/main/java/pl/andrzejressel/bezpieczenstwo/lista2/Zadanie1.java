package pl.andrzejressel.bezpieczenstwo.lista2;

import com.google.common.base.Splitter;
import pl.andrzejressel.bezpieczenstwo.lista2.dane.ZrodloDanych;
import pl.andrzejressel.bezpieczenstwo.lista2.kodowanie.SystemKodowania;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Zadanie1 {

    //Rozwiązanie: Ewa Wachowicz: "Lubię testować różne jądra"

    public void wykonaj(ZrodloDanych zrodloDanych, SystemKodowania systemKodowania) throws Exception {

        List<List<String>> kryptogramy = zrodloDanych.getDane().stream()
                .map(e -> Splitter.fixedLength(systemKodowania.getDlugosc()).splitToList(e))
                .collect(Collectors.toList());

        List<List<String>> klucze = new ArrayList<>();

        int dlugoscOstatniegoKryptogramu = kryptogramy.get(kryptogramy.size()-1).size();

        for (int i = 0; i < dlugoscOstatniegoKryptogramu; i++) {

            final int finalI = i;
            List<String> listaZnakow = kryptogramy.stream()
                    .filter(list -> finalI < list.size())
                    .map(list -> list.get(finalI))
                    .collect(Collectors.toList());


            List<String> mozliweKlucze = new ArrayList<>();


            for (String szyfr : systemKodowania.getAllCombinations()) {

                boolean znaleziono = true;

                for (String czescKryptogramu : listaZnakow) {

                    int znak = Integer.parseInt(szyfr, 2) ^ Integer.parseInt(czescKryptogramu, 2);

                    if (!systemKodowania.getWhitelist().contains(znak)) {
                        znaleziono = false;
                        break;
                    }

                }

                if (znaleziono) {
                    mozliweKlucze.add(szyfr);
                }


            }

            klucze.add(mozliweKlucze);

        }


        List<String> doDeszyfracji = kryptogramy.get(kryptogramy.size() - 1);


        for (int i = 0; i < doDeszyfracji.size(); i++) {

            String deszyfracja = doDeszyfracji.get(i);
            List<String> kluczeLocal = klucze.get(i);

            if (kluczeLocal.isEmpty()) {
                System.out.println("BRAK");
            } else {

                String toPrint = "";

                for (String mozliwyKlucz : kluczeLocal) {

                    int znak = Integer.parseInt(deszyfracja, 2) ^ Integer.parseInt(mozliwyKlucz, 2);
                    toPrint += ((char) znak + "_");

                }

                System.out.println(toPrint.subSequence(0, toPrint.length() - 1));

            }


        }


    }

}