package com.cy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync //加了此注解以后，服务在启动时会配置一个线程池
@SpringBootApplication
public class DbpmsAdminApplication {
	public static void main(String[] args) {
		SpringApplication.run(DbpmsAdminApplication.class, args);
	}
}
