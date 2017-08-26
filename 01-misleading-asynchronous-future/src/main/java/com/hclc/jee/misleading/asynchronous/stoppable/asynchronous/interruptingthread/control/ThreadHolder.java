package com.hclc.jee.misleading.asynchronous.stoppable.asynchronous.interruptingthread.control;

public class ThreadHolder {
    private volatile Thread thread;

    public void keep(Thread thread) {
        this.thread = thread;
    }

    public void interrupt() {
        thread.interrupt();
    }
}
