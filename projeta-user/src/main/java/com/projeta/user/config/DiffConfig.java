package com.projeta.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiffConfig {
	
	@Bean
	Diff diff() {
		return new Diff();
	}
}
