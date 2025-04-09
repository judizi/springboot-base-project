package com.judizi.base_api.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties("api")
public class ApiConfiguration {
    @NotBlank
    private String name;
    
    @NotBlank
    private String version;
}
