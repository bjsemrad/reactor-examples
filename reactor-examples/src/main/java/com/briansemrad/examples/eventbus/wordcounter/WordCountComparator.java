package com.briansemrad.examples.eventbus.wordcounter;

import java.util.Comparator;

/**
 * This comparator sorts a collection of WordCountPair's in descending order
 * based on the count in each object.
 * 
 * @author brian
 */
public class WordCountComparator implements Comparator<WordCountPair> {

    /**
     * @See Comparator<T>.compare
     */
    @Override
    public int compare(final WordCountPair o1, final WordCountPair o2) {
        return o2.getCount() - o1.getCount();
    }
    
}
