package org.csystem.application.runner;

import org.csystem.application.component.SharedObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.ExecutorService;

@Component
public class ProducerRunner implements ApplicationRunner {
    @Qualifier("csd-pool")
    private final ExecutorService m_threadPool;

    private final SharedObject m_sharedObject;

    private final Random m_random;

    @Value("${sleep.min:0}")
    private long m_min;

    @Value("${sleep.min:1}")
    private long m_max;

    @Value("${produce.count:99}")
    private int m_count;

    public ProducerRunner(ExecutorService threadPool, SharedObject sharedObject, Random random) {
        m_threadPool = threadPool;
        m_sharedObject = sharedObject;
        m_random = random;
    }

    private void producerCallback()
    {
        int i = 0;

        for (;;)
            try {
                Thread.sleep(Math.abs(m_random.nextLong()) % (m_max - m_min + 1) + m_min);
                m_sharedObject.setVal(++i);
                if(i == m_count)
                    break;
            } catch (InterruptedException ignore) {
            }

    }

    @Override
    public void run(ApplicationArguments args) {
        m_threadPool.execute((this::producerCallback));

        System.out.println("producer ends");
    }
}
