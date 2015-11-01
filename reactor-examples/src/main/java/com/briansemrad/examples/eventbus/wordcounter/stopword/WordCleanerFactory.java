package com.briansemrad.examples.eventbus.wordcounter.stopword;

/**
 * This class is responsible for creating instances or the WordCleaner
 * interface.
 * 
 * @author brian
 */
public final class WordCleanerFactory {
    
    private WordCleanerFactory(){}
    
    /**
     * Obtains an instance of the StopWordCleaner.s
     * @return 
     */
    public static WordCleaner getStopWordCleaner(){
        return new StopWordCleaner();
    }
}
