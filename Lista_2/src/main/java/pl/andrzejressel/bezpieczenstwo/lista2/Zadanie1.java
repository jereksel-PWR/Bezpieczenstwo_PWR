package pl.andrzejressel.bezpieczenstwo.lista2;

import com.google.common.base.Splitter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import pl.andrzejressel.bezpieczenstwo.lista2.dane.StronaWykladowcy;
import pl.andrzejressel.bezpieczenstwo.lista2.dane.ZrodloDanych;
import pl.andrzejressel.bezpieczenstwo.lista2.interfaces.Zadanie;
import pl.andrzejressel.bezpieczenstwo.lista2.kodowanie.ASCII;
import pl.andrzejressel.bezpieczenstwo.lista2.kodowanie.SystemKodowania;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Zadanie1 implements Zadanie {

    //Rozwiązanie: Ewa Wachowicz: "Lubię testować różne jądra"

    public void wykonaj(String indeks, SystemKodowania systemKodowania) throws Exception {

       // ASCII ascii = new ASCII();

        ZrodloDanych zrodloDanych = new StronaWykladowcy(indeks);

        /*
        String data = IOUtils.toString(new URL("http://zagorski.im.pwr.wroc.pl/courses/kbk2015/l1.php?id=" + indeks));

        data = StringUtils.substringBefore(data, "<br /><br /><br /><b>(zad 2)</b> znajdz klucz, ktorego 8 znakow to:");

        data = data.replace("<br /><br /><br /><br /><b>(zad 1) kryptogram do zdeszyfrowania:</b><br />", "<br /><br />kryptogram nr 99:<br />");

        List<String> dane = Arrays.asList(data.split("<br /><br />kryptogram nr (\\d)*:<br />"));

        List<List<String>> kryptogramy = dane.stream()
                .map(string -> Arrays.asList(StringUtils.split(string)))
                .filter(e -> !e.isEmpty())
                .collect(Collectors.toList());
*/

        List<String> dane = zrodloDanych.getDane();



        List<List<String>> kryptogramy = zrodloDanych.getDane().stream()
                .map(e -> Splitter.fixedLength(systemKodowania.getDlugosc()).splitToList(e))
                .collect(Collectors.toList());

        //StringUtils.

        List<List<String>> klucze = new ArrayList<>();

        int najdluzszyTekstDlugosc = kryptogramy.stream()
                .mapToInt(List::size)
                .max().getAsInt();

        for (int i = 0; i < najdluzszyTekstDlugosc; i++) {

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
                    //toPrint += ((char) znak + "{" + mozliwyKlucz + "}" + "_");

                }

                System.out.println(toPrint.subSequence(0, toPrint.length() - 1));

            }


        }


    }

}
