package com.ignaciorudyk.auth.autoconfigure;

import com.ignaciorudyk.auth.config.StarterAuthenticationProperties;
import com.ignaciorudyk.auth.config.security.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AutoConfiguration
@ConditionalOnProperty(
        prefix = "ignaciorudyk.authentication",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
@ComponentScan(
        value = "com.ignaciorudyk.auth",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = SecurityAutoConfiguration.class
        )
)
@EnableJpaRepositories("com.ignaciorudyk.auth.repository")
@EntityScan("com.ignaciorudyk.auth.repository.model")
@EnableConfigurationProperties(StarterAuthenticationProperties.class)
public class AuthAutoConfiguration {}