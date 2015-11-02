package com.briansemrad.examples.eventbus.wordcounter;

/**
 * This object is a simple representation of a word, 
 * paired with a count of that word.
 * 
 * @author brian
 */
public class WordCountPair {
    
    private final String word;
    private final int count;

    /**
     * Constructs a WordCountPair based on the passed in data.
     * @param word
     * @param count 
     */
    public WordCountPair(final String word, final int count) {
        this.word = word;
        this.count = count;
    }

    /**
     * Obtains the word contained in this pair
     * @return 
     */
    public String getWord() {
        return word;
    }

    /**
     * Obtains the count of the word contained in this pair
     * @return 
     */
    public int getCount() {
        return count;
    }
}
