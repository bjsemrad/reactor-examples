package com.briansemrad.examples.eventbus.wordcounter.cleaner;

/**
 * This factory is used to obtain instances of the
 * WordSplitter implementations.
 * 
 * @author brian
 */
public final class WordSplitterFactory {
    
    private WordSplitterFactory(){}
    
    /**
     * Obtains an implementation of a Word splitter that uses regular expressions.
     * @return 
     */
    public static WordSplitter getRegexWordSplitter(){
        return new RegexWordSplitter();
    }
    
}
