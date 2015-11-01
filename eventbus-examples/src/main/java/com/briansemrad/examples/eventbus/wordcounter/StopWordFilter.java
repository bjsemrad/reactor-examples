package com.briansemrad.examples.eventbus.wordcounter;


import com.briansemrad.examples.eventbus.wordcounter.stopword.WordCleaner;
import reactor.bus.EventBus;

/**
 * This filter is responsible for removing any stop words. It will read off the inbound queue and only push to the outbound queue
 * words which are not in its list.
 *
 * @author brian
 */
public class StopWordFilter extends AbstractFilter<String, String> implements Filter<String> {

    private final WordCleaner wordCleaner;


    public StopWordFilter(final String topic, final EventBus eventBus, final WordCleaner wordCleaner) {
        super(topic, eventBus);
        this.wordCleaner = wordCleaner;
    }

    @Override
    public void filter(final String itemToFilter) {
        //Completion signal only for the example, generally this type of code doesn't exit in an event processor
        if (COMPLETIONSIGNAL.equals(itemToFilter)) {
            notify(COMPLETIONSIGNAL);
        } else {
            if (wordCleaner.keepWord(itemToFilter)) {
                notify(itemToFilter);
            }
        }
    }
}
