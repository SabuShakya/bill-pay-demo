package com.billpayment.billpaydemo.configuration.proprties;

import com.billpayment.billpaydemo.configuration.YamlPropertySourceFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(value = "chitrawan")
@PropertySource(factory = YamlPropertySourceFactory.class,
        value = "classpath:application.yml")
@Getter
@Setter
public class ChitrawanProperties {

    private String authKey;

    private String authorizationHeader;
}
