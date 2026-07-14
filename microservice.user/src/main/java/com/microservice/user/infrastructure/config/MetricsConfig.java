package com.microservice.user.infrastructure.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public Counter userRegistrationsCounter(MeterRegistry registry) {
        return Counter.builder("user.registrations.total")
                .description("Total number of user registrations")
                .register(registry);
    }

    @Bean
    public Counter userLoginsCounter(MeterRegistry registry) {
        return Counter.builder("user.logins.total")
                .description("Total number of login attempts")
                .tag("status", "success")
                .register(registry);
    }

    @Bean
    public Counter userLoginsFailedCounter(MeterRegistry registry) {
        return Counter.builder("user.logins.total")
                .description("Total number of failed login attempts")
                .tag("status", "failed")
                .register(registry);
    }

    @Bean
    public Counter userProfileViewsCounter(MeterRegistry registry) {
        return Counter.builder("user.profile.views.total")
                .description("Total number of profile views")
                .register(registry);
    }
}
