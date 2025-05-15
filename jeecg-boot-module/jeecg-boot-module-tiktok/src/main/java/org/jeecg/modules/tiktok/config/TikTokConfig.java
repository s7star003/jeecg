package org.jeecg.modules.tiktok.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "tiktok")
public class TikTokConfig {
    private String appKey;
    private String appSecret;
    private String region;
    private String shopId;
    private String redirectUri;
}
