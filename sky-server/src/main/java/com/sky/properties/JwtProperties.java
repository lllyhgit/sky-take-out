package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sky.jwt")
@Data
public class JwtProperties {
    private String adminSecretKey;
    private long adminTtl = 7200000L;
    private String adminTokenName = "token";
    private String userSecretKey;
    private long userTtl = 2592000000L;
    private String userTokenName = "authentication";
}
