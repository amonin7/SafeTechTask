package com.safetech.jobtask.service1.config;

import com.safetech.jobtask.service1.publisher.MessagePublisher;
import com.safetech.jobtask.service1.publisher.MessagePublisherImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

@Configuration
public class RedisConfig {

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        final RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setValueSerializer(new GenericToStringSerializer<>(String.class));
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
    MessagePublisher redisPublisher() {
        return new MessagePublisherImpl(redisTemplate(), bytesTopic());
    }
}
