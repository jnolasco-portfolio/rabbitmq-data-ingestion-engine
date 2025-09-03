package cloud.jnolasco.rabbitmq.ingestor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CsvIngestorServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CsvIngestorServiceApplication.class, args);
	}

}
