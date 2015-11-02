package com.briansemrad.examples.eventbus.wordcounter;


import com.briansemrad.examples.eventbus.wordcounter.stopword.StopWordCleaner;

import java.util.Collections;

/**
 * This filter is responsible for removing any stop words. It will read off the inbound queue and only push to the outbound queue
 * words which are not in its list.
 *
 * @author brian
 */
public final class StopWordFilter {

    private static StopWordCleaner wordCleaner = new StopWordCleaner();

    public static String filter(final String itemToFilter) {
        if("COMPLETETIONSIGNAL".equals(itemToFilter)){
            return itemToFilter;
        }else {
            if (wordCleaner.keepWord(itemToFilter)) {
                return itemToFilter;
            }
            return null;
        }
    }
}
