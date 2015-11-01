package com.briansemrad.examples.eventbus.wordcounter.cleaner;

/**
 * This factory is used to obtain instances of the
 * FileCleaner implementations.
 * 
 * @author brian
 */
public final class FileCleanerFactory {
    
    private FileCleanerFactory(){}
    
    /**
     * Obtains an implementation of a FileCleaner that uses regular expressions.
     * @return 
     */
    public static FileCleaner getRegexFileCleaner(){
        return new RegexFileCleanser();
    }
    
}
