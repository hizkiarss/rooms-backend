package com.rooms.rooms.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

     @Bean
     public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
          RedisTemplate<String, String> template = new RedisTemplate<>();
          template.setConnectionFactory(connectionFactory);
          return template;
     }

     @Bean
     public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
          return new StringRedisTemplate(connectionFactory);
     }

     @Bean
     public ObjectMapper objectMapper() {
          ObjectMapper mapper = new ObjectMapper();
          return mapper;
     }
}