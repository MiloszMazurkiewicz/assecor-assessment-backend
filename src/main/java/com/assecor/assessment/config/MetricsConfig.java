package com.assecor.assessment.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for custom metrics.
 * This class defines business-specific metrics for the Person management API.
 */
@Configuration
public class MetricsConfig {

    /**
     * Counter for tracking total person retrievals.
     */
    @Bean
    public Counter personRetrievalCounter(MeterRegistry meterRegistry) {
        return Counter.builder("person.retrieval.total")
                .description("Total number of person retrieval operations")
                .register(meterRegistry);
    }

    /**
     * Counter for tracking person creations.
     */
    @Bean
    public Counter personCreationCounter(MeterRegistry meterRegistry) {
        return Counter.builder("person.creation.total")
                .description("Total number of person creation operations")
                .register(meterRegistry);
    }

    /**
     * Counter for tracking person updates.
     */
    @Bean
    public Counter personUpdateCounter(MeterRegistry meterRegistry) {
        return Counter.builder("person.update.total")
                .description("Total number of person update operations")
                .register(meterRegistry);
    }

    /**
     * Counter for tracking person deletions.
     */
    @Bean
    public Counter personDeletionCounter(MeterRegistry meterRegistry) {
        return Counter.builder("person.deletion.total")
                .description("Total number of person deletion operations")
                .register(meterRegistry);
    }

    /**
     * Counter for tracking color retrievals.
     */
    @Bean
    public Counter colorRetrievalCounter(MeterRegistry meterRegistry) {
        return Counter.builder("color.retrieval.total")
                .description("Total number of color retrieval operations")
                .register(meterRegistry);
    }

    /**
     * Counter for tracking color creations.
     */
    @Bean
    public Counter colorCreationCounter(MeterRegistry meterRegistry) {
        return Counter.builder("color.creation.total")
                .description("Total number of color creation operations")
                .register(meterRegistry);
    }

    /**
     * Counter for tracking color updates.
     */
    @Bean
    public Counter colorUpdateCounter(MeterRegistry meterRegistry) {
        return Counter.builder("color.update.total")
                .description("Total number of color update operations")
                .register(meterRegistry);
    }

    /**
     * Counter for tracking color deletions.
     */
    @Bean
    public Counter colorDeletionCounter(MeterRegistry meterRegistry) {
        return Counter.builder("color.deletion.total")
                .description("Total number of color deletion operations")
                .register(meterRegistry);
    }
}
