package com.briansemrad.examples.eventbus.wordcounter.cleaner;

/**
 * This implementation of the FileCleaner uses a regular expression to
 * clean the specified string of non alpha characters and spaces.
 *
 * @author brian
 */
class RegexFileCleanser implements FileCleaner {

    private final static String cleaningRegex = "[^A-Za-z ]";

    /**
     * @see FileCleaner.cleanse
     */
    @Override
    public String cleanse(final String dirty) {
        return dirty.replaceAll(cleaningRegex, " ");
    }
}
