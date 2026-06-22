package com.example.omnichannel_orders_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class OmnichannelOrdersApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OmnichannelOrdersApiApplication.class, args);
	}

}
