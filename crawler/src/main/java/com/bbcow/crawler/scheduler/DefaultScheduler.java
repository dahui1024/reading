package com.bbcow.crawler.scheduler;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.Scheduler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DefaultScheduler implements Scheduler{
    private BlockingQueue<Request> queue = new LinkedBlockingQueue();
    @Override
    public void push(Request request, Task task) {
        this.queue.add(request);
    }

    @Override
    public Request poll(Task task) {
        return (Request)this.queue.poll();
    }
}
