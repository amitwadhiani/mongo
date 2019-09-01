package co.arctern.api.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Provider-api Spring boot project
 */
@SpringBootApplication
public class ProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }

    public static ConfigurableApplicationContext run(String[] args) {
        return SpringApplication.run(ProviderApplication.class, args);
    }

}
