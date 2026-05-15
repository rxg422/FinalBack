package com.jbro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan({
	"com.jbro.auth.model.dao",
	"com.jbro.mypage.model.dao",
	"com.jbro.tour.model.dao"
})
@SpringBootApplication(scanBasePackages = {"com.jbro"})
public class JbroApplication {
	public static void main(String[] args) {
		SpringApplication.run(JbroApplication.class, args);
	}
}
