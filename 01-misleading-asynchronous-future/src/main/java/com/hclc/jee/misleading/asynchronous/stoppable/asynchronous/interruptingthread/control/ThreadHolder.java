package com.hclc.jee.misleading.asynchronous.stoppable.asynchronous.interruptingthread.control;

class ThreadHolder {
    private volatile Thread thread;

    void keep(Thread thread) {
        this.thread = thread;
    }

    void interrupt() {
        thread.interrupt();
    }
}
