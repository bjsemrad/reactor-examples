package com.briansemrad.examples.eventbus;

import com.briansemrad.examples.eventbus.wordcounter.*;
import reactor.Environment;
import reactor.rx.Streams;
import reactor.rx.broadcast.Broadcaster;

import java.util.List;


public class BroadcasterExample {

    public static void main(String[] args) throws InterruptedException {
        Environment.initialize();

        Broadcaster<String> broadcaster = Broadcaster.create(Environment.get());

        WordCounterConsumer consumer = new WordCounterConsumer();
        broadcaster.dispatchOn(Environment.get().getCachedDispatcher())
                .map(FileReaderFilter::filter)
                .flatMap(sentence -> Streams.from(sentence).map(s -> s))
                .map(StemmerFilter::filter)
                .map(StopWordFilter::filter)
                .filter(word -> word != null)
                .consume(consumer);
        long start = System.nanoTime();
        broadcaster.onNext("src/main/resources/AltText1.txt");
        broadcaster.onNext("src/main/resources/Text2.txt");
        broadcaster.onNext("src/main/resources/Text3.txt");
        broadcaster.onNext("src/main/resources/Text4.txt");
        broadcaster.onNext("COMPLETETIONSIGNAL");
        List<WordCountPair> wordCounterPairList = consumer.awaitCompletion();
        long end = System.nanoTime();
        double completionTime = (end - start) / 1000000000.0;
        System.out.println("Completion time: " + completionTime);
        wordCounterPairList.subList(0, 20).forEach((word) -> System.out.println("Found word " + word.getWord() + " with a count of " + word.getCount()));
    }
}
