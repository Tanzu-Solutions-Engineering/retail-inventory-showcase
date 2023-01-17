package com.vmare.retail.inventory.forcasting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

@Configuration
public class JsonConfig {
    private String dateFormat = "dd-MM-yyyy hh:mm:sss";

    @Bean
    public ObjectMapper objectMapper()
    {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        objectMapper.setDateFormat(df);
        return objectMapper;
    }
}
