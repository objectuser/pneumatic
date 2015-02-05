package com.surgingsystems.etl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.surgingsystems.etl.filter.Filter;

@Component
public class Container {

    private List<Filter> filters = new ArrayList<Filter>();

    private List<Thread> threads = new ArrayList<Thread>();

    public void addFilter(Filter filter) {
        filters.add(filter);
    }

    public void start() {
        // each filter gets its own thread
        for (Filter filter : filters) {
            if (filter instanceof Runnable) {
                Runnable runnable = (Runnable) filter;
                Thread thread = new Thread(runnable);
                if (filter.getName() != null) {
                    thread.setName(filter.getName());
                }
                thread.start();
                threads.add(thread);
            }
        }
    }

    public void waitForCompletion() throws Exception {
        for (Thread thread : threads) {
            thread.join();
        }
    }
}
