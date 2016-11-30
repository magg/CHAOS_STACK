package com.inria.spirals.mgonzale.reporter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ReporterConfiguration {
/*
    @Bean
    @ConditionalOnProperty("dataDog.apiKey")
    Reporter dataDogReporter(@Value("${dataDog.apiKey}") String apiKey,
                             @Value("${dataDog.appKey}") String appKey,
                             RestTemplate restTemplate,
                             @Value("${dataDog.tags:}") String[] tags) {
        return new DataDogReporter(apiKey, appKey, restTemplate, tags);
    }
    */
    @Bean
    @ConditionalOnMissingBean(Reporter.class)
    Reporter noOpReporter() {
        return new NoOpReporter();
    }

}