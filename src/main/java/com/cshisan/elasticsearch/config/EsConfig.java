package com.cshisan.elasticsearch.config;

import com.cshisan.elasticsearch.factory.PropertySourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author CShisan
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "es")
@PropertySource(value = "classpath:config/es.yml", factory = PropertySourceFactory.class)
public class EsConfig {
    private String host;
    private Integer port;
    private String scheme;
}
