package pl.andrzejressel.bezpieczenstwo.lista2.dane;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StronaWykladowcy implements ZrodloDanych {

    private String indeks;

    public StronaWykladowcy(String indeks) {
        this.indeks = indeks;
    }


    @Override
    public List<String> getDane() {

        String data;
        try {
            data = IOUtils.toString(new URL("http://zagorski.im.pwr.wroc.pl/courses/kbk2015/l1.php?id=" + indeks));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        data = StringUtils.substringBefore(data, "<br /><br /><br /><b>(zad 2)</b> znajdz klucz, ktorego 8 znakow to:");

        data = data.replace("<br /><br /><br /><br /><b>(zad 1) kryptogram do zdeszyfrowania:</b><br />", "<br /><br />kryptogram nr 99:<br />");

        List<String> dane = Arrays.asList(data.split("<br /><br />kryptogram nr (\\d)*:<br />"));

        return dane.stream().map(StringUtils::deleteWhitespace).filter(e -> !StringUtils.isEmpty(e)).collect(Collectors.toList());

    }
}
