package com.safetech.jobtask.service2.config;

import com.safetech.jobtask.service2.subscriber.MessageSubscriber;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

import java.util.concurrent.Executors;

@Configuration
public class RedisConfig {

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    @Qualifier("redisTemplate")
    public RedisTemplate<String, String> redisTemplate() {
        final RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setValueSerializer(new GenericToStringSerializer<String>(String.class));
        return template;
    }

    @Bean
    @Qualifier("bytes")
    ChannelTopic bytesTopic() {
        return new ChannelTopic("Bytes");
    }

    @Bean
    @Qualifier("signature")
    ChannelTopic signTopic() {
        return new ChannelTopic("Sign");
    }

    @Bean
    RedisMessageListenerContainer redisContainer(@Qualifier("subscriber") MessageSubscriber subscriber) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(new MessageListenerAdapter(subscriber), bytesTopic());
        container.setTaskExecutor(Executors.newFixedThreadPool(4));
        return container;
    }

}

