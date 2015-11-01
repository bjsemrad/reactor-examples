package com.briansemrad.examples.eventbus.wordcounter;


import com.briansemrad.examples.eventbus.wordcounter.stemmer.Stemmer;
import reactor.bus.EventBus;

/**
 * This class represents the filter which will handle the process of Stemming the
 * words that come into its inbound queue and produce its output on the outbound queue.
 *
 * @author brian
 */
public class StemmerFilter extends AbstractFilter<String, String> implements Filter<String> {

    public StemmerFilter(final String topic, final EventBus eventBus) {
        super(topic, eventBus);
    }

    @Override
    public void filter(final String itemToFilter) {
        //Completion signal only for the example, generally this type of code doesn't exit in an event processor
        if (COMPLETIONSIGNAL.equals(itemToFilter)) {
            notify(COMPLETIONSIGNAL);
        } else {
            Stemmer stemmer = new Stemmer();
            stemmer.add(itemToFilter.toCharArray(), itemToFilter.length());
            stemmer.stem();
            String outWord = new String(stemmer.getResultBuffer());
            notify(outWord.trim());
        }
    }
}
