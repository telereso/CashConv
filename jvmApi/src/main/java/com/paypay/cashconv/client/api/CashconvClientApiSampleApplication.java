package io.telereso.cashconv.client.api;

import io.telereso.cashconv.client.CashconvClientManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CashconvClientApiSampleApplication {

	public static void main(String[] args) {
		CashconvClientManager
				.builder()
				.build();

		SpringApplication.run(CashconvClientApiSampleApplication.class, args);
	}

}
