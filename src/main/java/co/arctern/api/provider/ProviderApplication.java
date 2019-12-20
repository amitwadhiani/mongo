package co.arctern.api.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Provider-api Spring boot project
 * enable scheduling for cron-job.
 * enable async for running separate thread(s) for cronjob.
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class ProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }

    public static ConfigurableApplicationContext run(String[] args) {
        return SpringApplication.run(ProviderApplication.class, args);
    }

}
