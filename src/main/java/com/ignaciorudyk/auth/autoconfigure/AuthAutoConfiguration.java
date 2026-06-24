package com.ignaciorudyk.auth.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AutoConfiguration
@ComponentScan("com.ignaciorudyk.auth")
@EnableJpaRepositories("com.ignaciorudyk.auth.repository")
@EntityScan("com.ignaciorudyk.auth.repository.model")
public class AuthAutoConfiguration {
}