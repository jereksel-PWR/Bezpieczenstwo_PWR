package pl.andrzejressel.bezpieczenstwo.lista2.kodowanie;

import java.util.ArrayList;
import java.util.List;

public class ASCII implements SystemKodowania {

    public ArrayList<Integer> whiteList = new ArrayList<>();

    public ASCII() {

        whiteList.add((int) ' ');
        whiteList.add((int) '!');
        whiteList.add((int) '.');
        whiteList.add((int) ',');
        whiteList.add((int) '?');
        whiteList.add((int) ':');
        whiteList.add((int) '-');
        whiteList.add((int) ';');
        whiteList.add((int) '(');
        whiteList.add((int) ')');
        whiteList.add((int) '"');
        whiteList.add((int) ';');

/*
        whiteList.add((int) '%');
        whiteList.add((int) '[');
        whiteList.add((int) ']');
        whiteList.add((int) '\'');
        whiteList.add((int) '{');
        whiteList.add((int) '}');
        whiteList.add((int) '_');
*/

        //0-9
        for (int i = 48; i <= 57; i++) {
            whiteList.add(i);
        }


        //A-Z
        for (int i = 65; i <= 90; i++) {
            whiteList.add(i);
        }

        //a-z
        for (int i = 97; i <= 122; i++) {
            whiteList.add(i);
        }


    }

    @Override
    public int getDlugosc() {
        return 8;
    }

    public List<String> getAllCombinations() {

        List<String> toReturn = new ArrayList<>();

        for (int i = 0; i <= 255; i++) {
            toReturn.add(Integer.toString(i, 2));
        }

        return toReturn;

    }

    @Override
    public List<Integer> getWhitelist() {
        return whiteList;
    }

}
