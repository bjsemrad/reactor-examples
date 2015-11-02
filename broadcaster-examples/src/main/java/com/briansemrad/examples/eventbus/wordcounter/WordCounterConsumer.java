package com.briansemrad.examples.eventbus.wordcounter;

import reactor.fn.Consumer;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * This filter is responsible for reading off of an inbound queue
 * and then building a word, count map to return to its calling thread.
 *
 * @author brian
 */
public final class WordCounterConsumer implements Consumer<String> {


    private final Map<String, Integer> countMap;
    private boolean complete = false;

    public WordCounterConsumer() {
        countMap = new ConcurrentHashMap<>();
    }


    public List<WordCountPair> awaitCompletion() throws InterruptedException {
        while (!complete) {
            Thread.sleep(500);
        }
        List<WordCountPair> wordCountPairs = countMap.entrySet().parallelStream().map(entry -> new WordCountPair(entry.getKey(), entry.getValue())).collect(Collectors.toList());
        Collections.sort(wordCountPairs, new WordCountComparator());
        return wordCountPairs;
    }

    @Override
    public void accept(String itemToFilter) {
        if ("COMPLETETIONSIGNAL".equals(itemToFilter)) {
            complete = true;
        } else {
            countMap.compute(itemToFilter, (key, oldVal) -> oldVal == null ? 1 : oldVal + 1);
        }
    }
}
