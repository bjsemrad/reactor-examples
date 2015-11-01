package com.briansemrad.examples.eventbus.wordcounter;

/**
 * Created by brian on 10/31/15.
 */
public interface Filter<Type> {

    String COMPLETIONSIGNAL = "COMPLETIONSIGNAL";

    void filter(final Type itemToFilter);
}
