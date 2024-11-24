package io.connectevent.connectevent.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("io.connectevent.connectevent")
@EnableJpaRepositories("io.connectevent.connectevent")
@EnableTransactionManagement
public class DomainConfig {
}
