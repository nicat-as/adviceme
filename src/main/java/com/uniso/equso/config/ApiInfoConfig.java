package com.uniso.equso.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.util.Properties;

@Configuration
@ConfigurationProperties(prefix = "api.info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiInfoConfig {
    private String version;
    private String title;
    private String description;

    @SneakyThrows
    @PostConstruct
    public void init() {
        Properties properties = new Properties();
        properties.load(new FileInputStream("version.properties"));
        this.version = properties.getProperty("version.semver");
    }

}
