/*package com.example.springpoliecobe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // your reactjs URL
                .allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE")
                .allowedHeaders("Content-Type" , "application/json") // Adjust headers you need to allow
                .allowCredentials(true); // Add only if you want to access cookie

                /*registry.addResourceHandler("/static/**")
                        .addResourceLocations("/WEB-INF/view/react/build/static/");
                registry.addResourceHandler("/*.js")
                        .addResourceLocations("/WEB-INF/view/react/build/");
                registry.addResourceHandler("/*.json")
                        .addResourceLocations("/WEB-INF/view/react/build/");
                registry.addResourceHandler("/*.ico")
                        .addResourceLocations("/WEB-INF/view/react/build/");
                registry.addResourceHandler("/index.html")
                        .addResourceLocations("/WEB-INF/view/react/build/index.html");*/
    /*}

}*/
