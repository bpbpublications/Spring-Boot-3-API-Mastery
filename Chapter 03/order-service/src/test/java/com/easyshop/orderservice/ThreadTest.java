package com.easyshop.orderservice;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.stream.IntStream;

class ThreadTest {


    @Test
    void virtualThread() throws InterruptedException {
        var virtualThreads = IntStream.rangeClosed(1, 10)
                .mapToObj(taskNumber ->
                        Thread.ofVirtual().unstarted(new Task(taskNumber))).toList();
        virtualThreads.forEach(Thread::start);
        for (Thread t : virtualThreads) {
            t.join();
        }
    }

    @Test
    void platformThread() throws InterruptedException {
        var platformThread = IntStream.rangeClosed(1, 10)
                .mapToObj(taskNumber -> Thread.ofPlatform().unstarted(new Task(taskNumber))).toList();

        platformThread.forEach(Thread::start);
        for (Thread t : platformThread) {
            t.join();
        }
    }

    class Task implements Runnable {
        private final int taskNumber;

        public Task(int taskNumber) {
            this.taskNumber = taskNumber;
        }

        @Override
        public void run() {
            if (taskNumber == 1) {
                System.out.println(Thread.currentThread());
            }
            try {
                Thread.sleep(Duration.ofMillis(10));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (taskNumber == 1) {
                System.out.println(Thread.currentThread());
            }
        }
    }
}



