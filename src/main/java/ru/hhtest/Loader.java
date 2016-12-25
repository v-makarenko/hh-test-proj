package ru.hhtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by VMakarenko on 21.12.2016.
 */
public class Loader {
    private static Logger LOG = LoggerFactory.getLogger(Loader.class);
    private static ExecutorService executor = Executors.newFixedThreadPool(10);

    public Queue<ArticleTask> getToDoQueue() {
        return toDoQueue;
    }

    private final Queue<ArticleTask> toDoQueue = new ConcurrentLinkedQueue<>();

    public AtomicInteger getTasksLeft() {
        return tasksLeft;
    }

    private final AtomicInteger tasksLeft = new AtomicInteger(0);


    /**
     * Load text from wikipedia pages directly into desination map
     *
     * @param startingUrl    url to start from
     * @param depth          loading depth, zero if load only startingUrl page
     */
    public void load(String startingUrl, int depth) {
        tasksLeft.incrementAndGet();
        toDoQueue.add(new ArticleTask(startingUrl, depth, this));
        while (tasksLeft.get() != 0) {
            ArticleTask task = toDoQueue.poll();
            if (task != null) {
                executor.execute(task);
               LOG.debug("added {} to execution queue", task.getUrl());
            }


       }
    }

    public void stop(){
        toDoQueue.clear();
        tasksLeft.set(0);
        executor.shutdown();
    }

    public String getStats() {
        return "Stats: \n" +
                "------------\n" +
                "size: " + Storage.INSTANCE.getStorage().size() + "\n" +
                "articles to load:" + tasksLeft.get() + "\n";
    }
}
