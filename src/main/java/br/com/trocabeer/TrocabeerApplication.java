package br.com.trocabeer;

import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "br.com.trocabeer")
public class TrocabeerApplication {

	private static Logger logger = LoggerFactory.getLogger(TrocabeerApplication.class);

	public static void main(String[] args) {
		logger.info("Iniciando a aplicação");
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		SpringApplication.run(TrocabeerApplication.class, args);
		logger.info("TrocaBeer iniciado.");
	}

}
