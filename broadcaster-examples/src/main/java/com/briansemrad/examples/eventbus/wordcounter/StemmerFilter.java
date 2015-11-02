package com.briansemrad.examples.eventbus.wordcounter;


import com.briansemrad.examples.eventbus.wordcounter.stemmer.Stemmer;

import java.util.Collections;
import java.util.stream.Stream;

/**
 * This class represents the filter which will handle the process of Stemming the
 * words that come into its inbound queue and produce its output on the outbound queue.
 *
 * @author brian
 */
public final class StemmerFilter {

    private StemmerFilter() {
    }

    public static String filter(final String itemToFilter) {
        if("COMPLETETIONSIGNAL".equals(itemToFilter)){
            return itemToFilter;
        }else {
            Stemmer stemmer = new Stemmer();
            stemmer.add(itemToFilter.toCharArray(), itemToFilter.length());
            stemmer.stem();
            String outWord = new String(stemmer.getResultBuffer());
            return outWord.trim();
        }
    }
}
