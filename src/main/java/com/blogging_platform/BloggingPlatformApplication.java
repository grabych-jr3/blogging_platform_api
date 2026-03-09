package com.blogging_platform;

import com.blogging_platform.dto.PostDTO;
import com.blogging_platform.models.Post;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@SpringBootApplication
@EnableCaching
public class BloggingPlatformApplication {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new JacksonJsonRedisSerializer<>(Post.class)));

        return RedisCacheManager
                .builder(connectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }

	public static void main(String[] args) {
		SpringApplication.run(BloggingPlatformApplication.class, args);
	}

}
