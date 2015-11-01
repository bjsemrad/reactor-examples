package com.briansemrad.examples.eventbus.wordcounter;

import reactor.bus.Event;
import reactor.bus.EventBus;

/**
 * Created by brian on 10/31/15.
 */
public abstract class AbstractFilter<In, Out> implements Filter<In> {

    private final EventBus eventBus;
    private final String topic;

    public AbstractFilter(final String topic, final EventBus eventBus) {
        this.eventBus = eventBus;
        this.topic = topic;
    }

    protected void notify(final Out out) {
        eventBus.notify(topic, Event.wrap(out));
    }
}
