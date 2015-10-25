package pl.andrzejressel.bezpieczenstwo.lista2;

import org.apache.commons.cli.*;
import pl.andrzejressel.bezpieczenstwo.lista2.dane.Plik;
import pl.andrzejressel.bezpieczenstwo.lista2.dane.StronaWykladowcy;
import pl.andrzejressel.bezpieczenstwo.lista2.dane.ZrodloDanych;
import pl.andrzejressel.bezpieczenstwo.lista2.kodowanie.ASCII;
import pl.andrzejressel.bezpieczenstwo.lista2.kodowanie.SystemKodowania;

import java.io.File;

public class Main {


    public static void main(String[] args) throws Exception {

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        Options options = getOptions();

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            showHelp();
            return;
        }


        String indeks = cmd.getOptionValue("indeks");
        String zadanie = cmd.getOptionValue("zadanie");
        String sposob = cmd.getOptionValue("sposob");


        ZrodloDanych zrodloDanych;

        switch (sposob) {
            case "plik":
                zrodloDanych = new Plik(new File(indeks));
                break;

            case "wykladowca":
                zrodloDanych = new StronaWykladowcy(indeks);
                break;

            default:
                showHelp();
                return;
        }

        SystemKodowania ascii = new ASCII();

        switch (zadanie) {
            case "1":
                new Zadanie1().wykonaj(zrodloDanych, ascii);
                break;
            case "2":
                new Zadanie2().wykonaj(indeks, ascii);
                break;
            default:
                showHelp();
                break;
        }

    }

    private static void showHelp() {
        Options options = getOptions();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("lista2", options);
    }

    private static Options getOptions() {

        Options options = new Options();

        Option zadanie = Option.builder()
                .hasArg()
                .desc("nr. zadania")
                .required()
                .longOpt("zadanie").build();

        Option sposob = Option.builder()
                .hasArg()
                .desc("wykladowca/plik")
                .required()
                .longOpt("sposob").build();

        Option indeks = Option.builder()
                .hasArg()
                .desc("nr. indeksu/plik")
                .required()
                .longOpt("indeks").build();

        options.addOption(zadanie);
        options.addOption(sposob);
        options.addOption(indeks);

        return options;
    }

}
