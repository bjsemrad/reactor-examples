package com.briansemrad.examples.eventbus;

import com.briansemrad.examples.eventbus.wordcounter.*;
import com.briansemrad.examples.eventbus.wordcounter.cleaner.FileCleanerFactory;
import com.briansemrad.examples.eventbus.wordcounter.cleaner.WordSplitterFactory;
import com.briansemrad.examples.eventbus.wordcounter.stopword.WordCleanerFactory;
import reactor.Environment;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.core.dispatch.RingBufferDispatcher;
import reactor.core.dispatch.TailRecurseDispatcher;
import reactor.core.dispatch.wait.AgileWaitingStrategy;
import reactor.jarjar.com.lmax.disruptor.dsl.ProducerType;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static reactor.bus.selector.Selectors.$;

/**
 * Created by brian on 10/31/15.
 * This example implementation demonstrates how to use an event bus for a word counting process.
 * It it using the TrailRecurseDispatcher as it is the most efficient when trying to processing a set of events.
 * As we can see it demonstrates how a single event bus can be used to handle multiple events.
 * This is almost as efficient as the @EventBusPipeFilterArchitectureExample with a single event bus handling
 * multiple events.
 */
public class EventBusExample {

    public static void main(String[] args) throws InterruptedException {
        Environment.initialize();

        WordCounterFilter counterFilter = new WordCounterFilter();
        EventBus eventBus = EventBus.create(new TailRecurseDispatcher());
        eventBus.on($("count"), (Event<String> event) -> counterFilter.filter(event.getData()));

        StemmerFilter stemmerFilter = new StemmerFilter("count", eventBus);
        eventBus.on($("stem"), (Event<String> event) -> stemmerFilter.filter(event.getData()));

        StopWordFilter stopWordFilter = new StopWordFilter("stem", eventBus, WordCleanerFactory.getStopWordCleaner());
        eventBus.on($("stop"), (Event<String> event) -> stopWordFilter.filter(event.getData()));

        FileReaderFilter fileReaderFilter = new FileReaderFilter("stop", eventBus, FileCleanerFactory.getRegexFileCleaner(), WordSplitterFactory.getRegexWordSplitter());
        eventBus.on($("file"), (Event<Path> path) -> fileReaderFilter.filter(path.getData()));

        long start = System.nanoTime();
        eventBus.notify("file", Event.wrap(Paths.get("src/main/resources/AltText1.txt", "")));
        eventBus.notify("file", Event.wrap(Paths.get("src/main/resources/Text2.txt", "")));
        eventBus.notify("file", Event.wrap(Paths.get("src/main/resources/Text3.txt", "")));
        eventBus.notify("file", Event.wrap(Paths.get("src/main/resources/Text4.txt", "")));
        eventBus.notify("file", Event.wrap(null));
        List<WordCountPair> wordCounterPairList = counterFilter.awaitCompletion();
        long end = System.nanoTime();
        double completionTime = (end - start) / 1000000000.0;
        System.out.println("Completion time: " + completionTime);
        wordCounterPairList.subList(0, 20).forEach((word) -> System.out.println("Found word " + word.getWord() + " with a count of " + word.getCount()));
    }
}
