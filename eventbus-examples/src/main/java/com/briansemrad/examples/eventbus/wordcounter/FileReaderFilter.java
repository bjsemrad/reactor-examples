package com.briansemrad.examples.eventbus.wordcounter;

import com.briansemrad.examples.eventbus.wordcounter.cleaner.FileCleaner;
import com.briansemrad.examples.eventbus.wordcounter.cleaner.WordSplitter;
import reactor.bus.EventBus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class FileReaderFilter extends AbstractFilter<Path, String> implements Filter<Path> {

    private final FileCleaner fileCleaner;
    private final WordSplitter wordSplitter;

    public FileReaderFilter(final String topic, final EventBus publishBus, final FileCleaner fileCleaner, final WordSplitter wordSplitter) {
        super(topic, publishBus);
        this.fileCleaner = fileCleaner;
        this.wordSplitter = wordSplitter;
    }

    @Override
    public void filter(final Path itemToFilter) {
        //Completion signal only for the example, generally this type of code doesn't exit in an event processor
        if(itemToFilter == null){
            notify(COMPLETIONSIGNAL);
        }else {
            try {
                Stream<String> fileLines = Files.lines(itemToFilter);
                fileLines.map(fileCleaner::cleanse)
                        .map(String::toLowerCase)
                        .map(wordSplitter::splitWords)
                        .flatMap(Collection::parallelStream)
                        .forEach(this::notify);
            } catch (IOException e) {
                Logger.getLogger(FileReaderFilter.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
}
