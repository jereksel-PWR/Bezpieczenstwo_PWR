package pl.andrzejressel.bezpieczenstwo.lista4;

import org.apache.commons.cli.*;

import java.math.BigInteger;

public class Main {


    public static void main(String[] args) throws Exception {

        /*
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
*/

      //  BigInteger liczba = Zadanie2.isPrime(64);

        //System.out.println(liczba);

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
