package pl.diminuen.propertysalessystem;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.diminuen.propertysalessystem.models.*;
import pl.diminuen.propertysalessystem.repositories.*;
import pl.diminuen.propertysalessystem.security.oauth2.OAuth2Provider;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class PropertySalesSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(PropertySalesSystemApplication.class, args);
	}

}
