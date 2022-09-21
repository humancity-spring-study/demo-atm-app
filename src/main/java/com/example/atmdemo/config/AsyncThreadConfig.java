package com.example.atmdemo.config;

import java.util.Map;
import java.util.concurrent.Executor;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncThreadConfig extends AsyncConfigurerSupport {

    private final int corePoolSize;
    private final int maxPoolSize;
    private final int queueCapacity;

    public AsyncThreadConfig(
        @Value("${pointshop.async.core-pool-size:4}") int corePoolSize,
        @Value("${pointshop.async.max-pool-size:16}") int maxPoolSize,
        @Value("${pointshop.async.queue-capacity:512}") int queueCapacity
    ) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.queueCapacity = queueCapacity;
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);

//        executor.setTaskDecorator(new ClonedTaskDecorator());
        executor.setThreadNamePrefix("async-task-");
        executor.setThreadGroupName("async-group");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        return executor;
    }

    public static class ClonedTaskDecorator implements TaskDecorator {

        @Override
        public Runnable decorate(Runnable runnable) {
            Map<String, String> callerThreadContext = MDC.getCopyOfContextMap();
            return () -> {
                MDC.setContextMap(callerThreadContext);
                runnable.run();
            };
        }
    }
}
