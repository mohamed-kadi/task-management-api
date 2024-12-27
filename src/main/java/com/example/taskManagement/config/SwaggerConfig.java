package com.example.taskManagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Contact contact = new Contact()
            .name("Mohamed Kadi")
            .email("mkadi@mailfence.com")
            .url("https://github.com/mohamed-kadi");
            
        License mitLicense = new License()
            .name("MIT License")
            .url("https:opensource.org/licenses/MIT");
        
        Info info = new Info()
            .title("Task Management API")
            .version("1.0")
            .description("Spring Boot REST API for Task Management.\n\n" + 
                         "## Developer Information\n" + 
                         "- Java Developer\n" + 
                         "- Spring Boot Specialist\n" + 
                         "- Open to Opportunities ")
            .contact(contact)
            .license(mitLicense);
        return new OpenAPI().info(info);
        
        
    }

}
