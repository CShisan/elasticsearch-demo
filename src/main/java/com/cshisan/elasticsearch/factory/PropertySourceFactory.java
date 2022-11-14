package com.cshisan.elasticsearch.factory;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.lang.NonNull;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

/**
 * @author CShisan
 */
public class PropertySourceFactory implements org.springframework.core.io.support.PropertySourceFactory {
    private static final String SUFFIX_YML = ".yml";
    private static final String SUFFIX_YAML = ".yaml";
    private static final String CONNECTIVES = "-";
    private static final String ENV = "self";

    @NonNull
    @Override
    public PropertySource<?> createPropertySource(String name, @NonNull EncodedResource encodedResource) throws IOException {
        Resource resource = encodedResource.getResource();
        if (resource instanceof ClassPathResource) {
            if (ObjectUtils.isEmpty(name)) {
                String filename = resource.getFilename();
                name = Optional.ofNullable(filename).orElse("");
            }

            ClassPathResource classPathResource = (ClassPathResource) resource;
            String path = classPathResource.getPath().replace(".", CONNECTIVES.concat(ENV).concat("."));
            ClassPathResource prioritized = new ClassPathResource(path);
            if (prioritized.exists()) {
                // 若多环境配置存在则优先加载多环境配置
                name = Optional.ofNullable(prioritized.getFilename()).orElse("");
                encodedResource = new EncodedResource(prioritized);
            } else if (!resource.exists()) {
                // 环境资源不存在 且 resource不存在则直接创建新文件返回
                return new PropertiesPropertySource(name, new Properties());
            }

            // 如果是yml/yaml文件则创建对应的文件返回
            if (name.endsWith(SUFFIX_YML) || name.endsWith(SUFFIX_YAML)) {
                Properties properties = loadYaml(encodedResource);
                return new PropertiesPropertySource(name, properties);
            }
        }
        return new ResourcePropertySource(resource);
    }

    /**
     * 加载yml资源
     *
     * @param resource resource
     * @return properties
     */
    private Properties loadYaml(EncodedResource resource) {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(resource.getResource());
        factory.afterPropertiesSet();
        return factory.getObject();
    }
}
