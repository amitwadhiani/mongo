package co.arctern.api.provider.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Component scan for dao layers / repositories in the project
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "co.arctern.api.provider.dao")
public class DatasourceConfig {
}