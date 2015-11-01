package com.briansemrad.examples.eventbus.wordcounter.stopword;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class is responsible for cleaning the words which it contains from the stream
 * in the pipe and filter.
 *
 * @author brian
 */
class StopWordCleaner implements WordCleaner {

    private static final String stopWordLocation = "/stopwords.txt";
    private final Map<String, String> stopWords;

    /**
     * Constructs the stop word cleaner using the stopword list contained in the jar file.
     * It also add's "" and " " as stop words incase the previous setup didn't clean those out.
     */
    public StopWordCleaner() {
        Stream<String> lines = null;
        try {
            lines = Files.lines(Paths.get(StopWordCleaner.class.getResource(stopWordLocation).toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Failed to initialize stopwords");
        }
        stopWords = lines.map(String::trim).collect(Collectors.toConcurrentMap(word -> word, word -> word, (word1, word2) -> word1));
        stopWords.put("", "");
        stopWords.put(" ", " ");
    }

    /**
     * Returns whether or not the specified word should be kept.
     *
     * @param word
     * @return
     */
    @Override
    public boolean keepWord(final String word) {
        return !stopWords.containsKey(word);
    }
}
