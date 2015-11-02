package com.briansemrad.examples.eventbus.wordcounter;

import com.briansemrad.examples.eventbus.wordcounter.cleaner.RegexFileCleanser;
import com.briansemrad.examples.eventbus.wordcounter.cleaner.RegexWordSplitter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class FileReaderFilter {

    private FileReaderFilter() {

    }

    public static List<String> filter(final String itemToFilter) {
        if("COMPLETETIONSIGNAL".equals(itemToFilter)){
            return Collections.singletonList(itemToFilter);
        }else {
            try {
                Stream<String> fileLines = Files.lines(Paths.get(itemToFilter, ""));
                return fileLines.map(RegexFileCleanser::cleanse)
                        .map(String::toLowerCase)
                        .map(RegexWordSplitter::splitWords)
                        .flatMap(Collection::parallelStream)
                        .collect(Collectors.toList());
            } catch (IOException e) {
                Logger.getLogger(FileReaderFilter.class.getName()).log(Level.SEVERE, null, e);
            }
            return Collections.emptyList();
        }
    }
}
