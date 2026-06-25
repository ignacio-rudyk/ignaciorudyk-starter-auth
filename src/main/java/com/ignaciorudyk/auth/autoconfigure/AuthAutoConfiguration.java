package com.ignaciorudyk.auth.autoconfigure;

import com.ignaciorudyk.auth.config.StarterAuthentitcationProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AutoConfiguration
@ConditionalOnProperty(
        prefix = "ignaciorudyk.authentication",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
@ComponentScan("com.ignaciorudyk.auth")
@EnableJpaRepositories("com.ignaciorudyk.auth.repository")
@EntityScan("com.ignaciorudyk.auth.repository.model")
@EnableConfigurationProperties(StarterAuthentitcationProperties.class)
public class AuthAutoConfiguration {
}