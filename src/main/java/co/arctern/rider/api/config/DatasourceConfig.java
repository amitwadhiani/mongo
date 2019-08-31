package co.arctern.rider.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Component scan for dao repositories in the project
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "co.arctern.rider.api.dao")
public class DatasourceConfig {
}