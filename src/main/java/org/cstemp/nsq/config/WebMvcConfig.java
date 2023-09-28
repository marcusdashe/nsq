package org.cstemp.nsq.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author MarcusDashe ChibuezeHarry
 * @version 2.0
 *
 */

@Configuration
public class WebMvcConfig  implements WebMvcConfigurer {

    private final long MAX_AGE_SECS = 36000;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("HEADS", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(MAX_AGE_SECS);
    }

}
