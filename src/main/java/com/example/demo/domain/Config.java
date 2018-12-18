package com.example.demo.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by TC20021 on 2018/11/14.
 */
@Component
@ConfigurationProperties(prefix = "config")
@Data
public class Config {

    String  code;

    List<String> hobby;

    String  name;
}
