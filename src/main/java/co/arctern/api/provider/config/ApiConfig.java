package co.arctern.api.provider.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Internal api component scan for external jars
 */
@Configuration
@ComponentScan("co.arctern.api.internal")
public class ApiConfig {

}
