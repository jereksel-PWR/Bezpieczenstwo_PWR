package pl.andrzejressel.bezpieczenstwo.lista2.dane;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Plik implements ZrodloDanych {

    File file;

    public Plik(File file) {
        this.file = file;
    }

    @Override
    public List<String> getDane() {
        try {
            return FileUtils.readLines(file).stream().map(StringUtils::deleteWhitespace).filter(e -> !StringUtils.isEmpty(e)).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
