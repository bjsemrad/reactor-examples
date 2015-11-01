package com.briansemrad.examples.eventbus.wordcounter.cleaner;

import java.util.List;

/**
 * This interface contains all of the operations of implementations
 * that take a line of text and split it into words.
 * 
 * @author brian
 */
public interface WordSplitter {

    /**
     * Splits the specified line into a list of words.
     * Each implementation decides how to implement this operation.
     * 
     * @param line
     * @return 
     */
    List<String> splitWords(final String line);
    
}
