package com.surgingsystems.etl.pipe;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.surgingsystems.etl.record.Record;

/**
 * A pipe using a {@link BlockingQueue} as its underlying implementation.
 */
public class BlockingQueuePipe implements Pipe {

    private static Logger logger = LogManager.getFormatterLogger(BlockingQueuePipe.class);

    @Value("${blockingQueuePipe.size}")
    private int size = 10;
    
    @Value("${blockingQueuePipe.timeoutMilliseconds}")
    private long timeout = 1000;
    
    private BlockingQueue<Record> queue;
    
    private boolean inputClosed = false;
    
    @PostConstruct
    public void initialize() {
        queue = new LinkedBlockingQueue<Record>(size);
    }

    @Override
    public void put(Record record) {
	try {
	    queue.put(record);
	} catch (RuntimeException e) {
	    throw logger.throwing(e);
	} catch (Exception e) {
	    throw logger.throwing(new RuntimeException(e));
	}
    }

    @Override
    public Record pull() {
	try {
	    return queue.poll(timeout, TimeUnit.MILLISECONDS);
	} catch (RuntimeException e) {
	    throw logger.throwing(e);
	} catch (Exception e) {
	    throw logger.throwing(new RuntimeException(e));
	}
    }
    
    @Override
    public void closedForInput() {
	inputClosed = true;
    }
    
    @Override
    public boolean isComplete() {
	return queue.isEmpty() && inputClosed;
    }
    
    @Override
    public String toString() {
	return String.format("%s(%s)", getClass().getSimpleName(), queue);
    }
}
