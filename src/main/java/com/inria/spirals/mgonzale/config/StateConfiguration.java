package com.inria.spirals.mgonzale.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.inria.spirals.mgonzale.domain.StateProvider;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

@Configuration
public class StateConfiguration {

    @Bean
    @ConditionalOnProperty("vcap.services.chaos-lemur-persistence.credentials.hostname")
    JedisPool jedisPool(@Value("${vcap.services.chaos-lemur-persistence.credentials.hostname}") String hostname,
                        @Value("${vcap.services.chaos-lemur-persistence.credentials.port}") Integer port,
                        @Value("${vcap.services.chaos-lemur-persistence.credentials.password}") String password) {
        return new JedisPool(new JedisPoolConfig(), hostname, port, Protocol.DEFAULT_TIMEOUT, password);
    }

    @Bean
    @ConditionalOnBean(JedisPool.class)
    StateProvider redisStateProvider(JedisPool jedisPool) {
        return new RedisStateProvider(jedisPool);
    }

    @Bean
    @ConditionalOnMissingBean(StateProvider.class)
    StateProvider simpleStateProvider() {
        return new SimpleStateProvider();
    }

}
