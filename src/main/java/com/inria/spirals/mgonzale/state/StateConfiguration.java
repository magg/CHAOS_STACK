package com.inria.spirals.mgonzale.state;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

@Configuration
public class StateConfiguration {

    @Bean
    @ConditionalOnProperty("redis.hostname")
    JedisPool jedisPool(@Value("${redis.hostname}") String hostname,
                        @Value("${redis.port}") Integer port,
                        @Value("${redis.password}") String password) {
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
