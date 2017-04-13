package com.nicolas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.servlet.annotation.MultipartConfig;

@SpringBootApplication
@MultipartConfig(
		location="/tmp",
		fileSizeThreshold=1024*1024,    // 1 MB
		maxFileSize=1024*1024*5,        // 5 MB
		maxRequestSize=1024*1024*5*5    // 25 MB
)
public class FinalassesmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinalassesmentApplication.class, args);
	}
}
