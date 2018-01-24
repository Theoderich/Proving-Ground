package de.theo.pg.provingground;

import de.theo.pg.provingground.persistence.JooqPersistence;
import de.theo.pg.provingground.persistence.MockPersistence;
import de.theo.pg.provingground.persistence.Persistence;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.net.URISyntaxException;

@SpringBootApplication
public class ProvingGroundApplication {


	public static void main(String[] args) {
		SpringApplication.run(ProvingGroundApplication.class, args);
	}
}
