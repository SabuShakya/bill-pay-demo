package com.billpayment.billpaydemo.configuration.proprties;


import com.billpayment.billpaydemo.configuration.YamlPropertySourceFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(value = "khanepani")
@PropertySource(factory = YamlPropertySourceFactory.class,
        value = "classpath:application.yml")
@Getter
@Setter
public class KhanepaniProperties {

    private String username;

    private String password;

    private String grantType;

    private String authorizationHeader;
}
