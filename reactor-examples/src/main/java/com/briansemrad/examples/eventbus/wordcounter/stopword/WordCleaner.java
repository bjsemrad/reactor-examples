package com.briansemrad.examples.eventbus.wordcounter.stopword;

/**
 * This interface represents all operations which determines
 * whether or not to keep a word.
 * 
 * @author brian
 */
public interface WordCleaner {

    /**
     * Decides whether or not to keep a word based on cleaning rules.
     * @param word
     * @return 
     */
    boolean keepWord(String word);
    
}
