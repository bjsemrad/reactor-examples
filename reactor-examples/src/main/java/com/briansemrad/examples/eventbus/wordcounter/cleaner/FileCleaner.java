package com.briansemrad.examples.eventbus.wordcounter.cleaner;

/**
 * This interface is used to represent a file cleaner
 * and the operations it can perform.
 * 
 * @author brian
 */
public interface FileCleaner {

    /**
     * Takes the specified String and removes all non alpha characters (except spaces).
     * Each implementation can decided on how this will be done.
     * @param dirty
     * @return 
     */
    String cleanse(final String dirty);
    
}
