package com.briansemrad.examples.eventbus;

import com.briansemrad.examples.eventbus.wordcounter.*;
import com.briansemrad.examples.eventbus.wordcounter.cleaner.FileCleanerFactory;
import com.briansemrad.examples.eventbus.wordcounter.cleaner.WordSplitterFactory;
import com.briansemrad.examples.eventbus.wordcounter.stopword.WordCleanerFactory;
import reactor.Environment;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.core.dispatch.RingBufferDispatcher;
import reactor.core.dispatch.wait.AgileWaitingStrategy;
import reactor.jarjar.com.lmax.disruptor.dsl.ProducerType;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static reactor.bus.selector.Selectors.$;

/**
 * Created by brian on 10/31/15.
 * This example implementation demonstrates a pipe and filter architecture for processing the word counting process.
 * It it using Ring Buffers as the queueing mechanism between steps in the data flow.  In addition to showing the pipe and filter
 * architecture the purpose is to show an example of how you can use the EventBus in Reactor to pass data between processes
 * async and efficiently.  This is a trivial example, but it shows the power of the reactor framework and how it can be used
 * to build a system that has a stream of data processing to do.
 *
 */
public class EventBusPipeFilterArchitectureExample {

    private static final int BUFFER_SIZE = 4096;

    public static void main(String[] args) throws InterruptedException {
        Environment.initialize();

        WordCounterFilter counterFilter = new WordCounterFilter();
        EventBus wordCounterEventBus = EventBus.create(new RingBufferDispatcher("WordCounter", BUFFER_SIZE, null, ProducerType.MULTI, new AgileWaitingStrategy()));
        wordCounterEventBus.on($("word"), (Event<String> event) -> counterFilter.filter(event.getData()));

        StemmerFilter stemmerFilter = new StemmerFilter("word", wordCounterEventBus);
        EventBus stemmerEventBus = EventBus.create(new RingBufferDispatcher("Stemmer", BUFFER_SIZE, null, ProducerType.MULTI, new AgileWaitingStrategy()));
        stemmerEventBus.on($("word"), (Event<String> event) -> stemmerFilter.filter(event.getData()));

        StopWordFilter stopWordFilter = new StopWordFilter("word", stemmerEventBus, WordCleanerFactory.getStopWordCleaner());
        EventBus stopWordEventBus = EventBus.create(new RingBufferDispatcher("StopWord", BUFFER_SIZE, null, ProducerType.MULTI, new AgileWaitingStrategy()));
        stopWordEventBus.on($("word"), (Event<String> event) -> stopWordFilter.filter(event.getData()));

        FileReaderFilter fileReaderFilter = new FileReaderFilter("word", stopWordEventBus, FileCleanerFactory.getRegexFileCleaner(), WordSplitterFactory.getRegexWordSplitter());
        EventBus fileEventBus = EventBus.create(new RingBufferDispatcher("File", BUFFER_SIZE, null, ProducerType.SINGLE, new AgileWaitingStrategy()));
        fileEventBus.on($("file"), (Event<Path> path) -> fileReaderFilter.filter(path.getData()));

        long start = System.nanoTime();
        fileEventBus.notify("file", Event.wrap(Paths.get("src/main/resources/AltText1.txt", "")));
        fileEventBus.notify("file", Event.wrap(Paths.get("src/main/resources/Text2.txt", "")));
        fileEventBus.notify("file", Event.wrap(Paths.get("src/main/resources/Text3.txt", "")));
        fileEventBus.notify("file", Event.wrap(Paths.get("src/main/resources/Text4.txt", "")));
        //Used as a kill signal for the purpose of knowing when there are no more events to process.
        //Used for the purpose of timing the process on the set of files above.
        fileEventBus.notify("file", Event.wrap(null));
        //Block the main thread until the counter is "completed"
        List<WordCountPair> wordCounterPairList = counterFilter.awaitCompletion();
        long end = System.nanoTime();
        double completionTime = (end - start) / 1000000000.0;

        System.out.println("Completion time: " + completionTime);
        wordCounterPairList.subList(0, 20).forEach((word) -> System.out.println("Found word " + word.getWord() + " with a count of " + word.getCount()));
    }
}
