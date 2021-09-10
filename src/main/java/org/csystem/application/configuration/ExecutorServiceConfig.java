package org.csystem.application.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.concurrent.ExecutorService;
import static java.util.concurrent.Executors.newCachedThreadPool;
import static java.util.concurrent.Executors.newFixedThreadPool;

@Configuration
public class ExecutorServiceConfig {
    @Bean(name = "csd-pool")
    @Scope("singleton")
    public ExecutorService getExecutorService(@Value("${thread.count:-1}") int count)
    {
        return count == -1 ? newCachedThreadPool() : newFixedThreadPool(count);
    }
}


