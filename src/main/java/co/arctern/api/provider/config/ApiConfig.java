package co.arctern.api.provider.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Internal api component scan for external jars
 */
@Configuration
@ComponentScan("co.arctern.api.internal")
public class ApiConfig {

    @Bean
    public SpelAwareProxyProjectionFactory projectionFactory() {
        return new SpelAwareProxyProjectionFactory();
    }

    /**
     * creating a thread pool for task execution (used in scheduler).
     *
     * @return
     */
    @Bean("threadPoolTaskExecutor")
    public TaskExecutor createThreadPool() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(1);
        threadPoolTaskExecutor.setMaxPoolSize(2000);
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskExecutor.setThreadNamePrefix("Async call -> ");
        return threadPoolTaskExecutor;
    }
}
