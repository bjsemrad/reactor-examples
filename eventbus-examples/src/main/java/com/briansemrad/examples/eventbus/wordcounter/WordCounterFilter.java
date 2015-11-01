package com.briansemrad.examples.eventbus.wordcounter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This filter is responsible for reading off of an inbound queue
 * and then building a word, count map to return to its calling thread.
 *
 * @author brian
 */
public class WordCounterFilter implements Filter<String> {


    private final Map<String, Integer> countMap;
    private boolean complete = false;

    public WordCounterFilter() {
        countMap = new ConcurrentHashMap<>();
    }

    @Override
    public void filter(final String itemToFilter) {
        //Completion signal only for the example, generally this type of code doesn't exit in an event processor
        if (COMPLETIONSIGNAL.equals(itemToFilter)) {
            complete = true;
        }
        countMap.compute(itemToFilter, (key, oldVal) -> oldVal == null ? 1 : oldVal + 1);

    }

    public List<WordCountPair> awaitCompletion() throws InterruptedException {
        while (!complete) {
            Thread.sleep(500);
        }
        List<WordCountPair> wordCountPairs = countMap.entrySet().parallelStream().map(entry -> new WordCountPair(entry.getKey(), entry.getValue())).collect(Collectors.toList());
        Collections.sort(wordCountPairs, new WordCountComparator());
        return wordCountPairs;
    }
}
