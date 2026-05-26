package com.jbro.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.jbro.admin.interceptor.VisitorInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	 private final VisitorInterceptor visitorInterceptor; // 추가
	 
	  public WebConfig(VisitorInterceptor visitorInterceptor) { // 추가
	        this.visitorInterceptor = visitorInterceptor;
	    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:3000", "http://localhost:3001")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
  
  	@Override
	 public void addResourceHandlers(ResourceHandlerRegistry registry) {
		  Path uploadPath = Paths.get("uploads").toAbsolutePath().normalize();
		  registry.addResourceHandler("/uploads/**").addResourceLocations(uploadPath.toUri().toString() + "/");
	 }
  	 @Override // 추가
     public void addInterceptors(InterceptorRegistry registry) {
         registry.addInterceptor(visitorInterceptor)
             .addPathPatterns("/api/**")
             .excludePathPatterns("/api/admin/**");
     }
}