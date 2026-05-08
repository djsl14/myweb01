package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sky.map")
@Data
//百度地图api调用配置项
public class BaiduMapProperties {
    private String address;
    private String ak;
    private String geocodingUrl;
    private String directionUrl;
}
