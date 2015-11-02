package com.briansemrad.examples.eventbus.wordcounter.cleaner;

import java.util.Arrays;
import java.util.List;

/**
 * This is an implementation of the word splitter interface
 * which uses regular expressions to split out words.
 * 
 * @author brian
 */
public class RegexWordSplitter {

    public static List<String> splitWords(final String line) {
        String[] lineWordsSplit = line.split("\\s+");
        return Arrays.asList(lineWordsSplit);
    }
}
