/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.util;

import java.util.LinkedList;
import java.util.Queue;

public class ProcessingQueue<E> {
    private Queue<E> _queue;
    private Boolean _finished;
    private Object hold;

    public ProcessingQueue() {
        _queue = new LinkedList<E>();
        _finished = false;
        hold = new Object();
    }

    public void finish() {
        synchronized (_finished) {
            _finished = true;
        }
        synchronized (hold) {
            hold.notifyAll();
        }
    }

    public boolean hasFinished() {
        synchronized (_finished) {
            synchronized (_queue) {
                return _finished.booleanValue() && _queue.isEmpty();
            }
        }
    }

    public void enqueue(E e) {
        boolean notify = false;
        synchronized (_queue) {
            if (_queue.isEmpty()) {
                _queue.add(e);
                notify = true;
            } else {
                _queue.add(e);
            }
        }
        if (notify) {
            synchronized (hold) {
                hold.notifyAll();
            }
        }
    }

    private boolean isEmpty() {
        synchronized (_queue) {
            return _queue.isEmpty();
        }
    }

    public E dequeue() {
        if (!this.hasFinished() && this.isEmpty()) {
            try {
                synchronized (hold) {
                    hold.wait();
                }
            } catch (InterruptedException e) {
                return null;
            }
        }
        synchronized (_queue) {
            return _queue.poll();
        }
    }
}
