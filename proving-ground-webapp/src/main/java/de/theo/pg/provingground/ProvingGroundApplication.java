package de.theo.pg.provingground;

import de.theo.pg.provingground.persistence.MockPersistence;
import de.theo.pg.provingground.persistence.Persistence;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProvingGroundApplication {

	@Bean
    public Persistence persistence(){
	    return new MockPersistence();
    }

	public static void main(String[] args) {
		SpringApplication.run(ProvingGroundApplication.class, args);
	}
}
