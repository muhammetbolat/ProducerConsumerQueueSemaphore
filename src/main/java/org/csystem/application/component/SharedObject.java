package org.csystem.application.component;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.Semaphore;

@Component
public class SharedObject {
    private final Semaphore m_producerSemaphore;
    private final Semaphore m_consumerSemaphore;
    private final int [] m_queue;
    private int m_head;
    private int m_tail;

    public SharedObject(@Qualifier("producerSemaphore") Semaphore m_producerSemaphore,
                        @Qualifier("consumerSemaphore") Semaphore m_consumerSemaphore,
                        int[] m_queue)
    {
        this.m_producerSemaphore = m_producerSemaphore;
        this.m_consumerSemaphore = m_consumerSemaphore;
        this.m_queue = m_queue;
    }

    public void setVal(int val)
    {
        try {
            m_producerSemaphore.acquire();
        } catch (InterruptedException ignore) {
        }

        m_queue[m_tail++] = val;

        m_tail %= m_queue.length;

        m_consumerSemaphore.release(m_queue.length);
    }

    public int getVal()
    {
        int val;

        try {
            m_consumerSemaphore.acquire(m_queue.length);
        } catch (InterruptedException ignore) {
        }

        val = m_queue[m_head++];
        m_head %= m_queue.length;
        m_producerSemaphore.release(m_queue.length);

        return val;
    }


}

