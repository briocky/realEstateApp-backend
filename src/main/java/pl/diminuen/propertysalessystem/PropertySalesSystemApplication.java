package pl.diminuen.propertysalessystem;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
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

	@Bean
	public CommandLineRunner commandLineRunner(UserRepository userRepository, RoleRepository roleRepository, OfferRepository offerRepository) {
		return args -> {
//			OfferType saleOfferType = new OfferType(EOfferType.SALE);
//			OfferType rentOfferType = new OfferType(EOfferType.RENT);
//
//			OfferStatus activeStatus = new OfferStatus(EOfferStatus.ACTIVE);
//
//			Offer offer = new Offer("MIESZKANIE ŁADNE", saleOfferType);
//			Offer offer2 = new Offer("MIESZKANIE BRZYDKIE", saleOfferType);

			Role roleUser = new Role(ERole.ROLE_USER);
			Role roleModerator = new Role(ERole.ROLE_MODERATOR);
			Role roleAdmin = new Role(ERole.ROLE_ADMIN);

			List<Role> modRoles = new ArrayList<>(List.of(roleUser,roleModerator));
			List<Role> userRoles = new ArrayList<>(List.of(roleUser));
			List<Role> adminRoles = new ArrayList<>(List.of(roleUser,roleAdmin));

			User u1 = new User("Jan", "Kowalski", "userjanek123", "janek123@wp.pl", "janek", "123456789", true, LocalDateTime.now(ZoneId.systemDefault()), OAuth2Provider.LOCAL,userRoles);
			User u2 = new User("Marek", "Kowalski", "adminmarek123", "marek@wp.pl", "marek", "123456789", true, LocalDateTime.now(ZoneId.systemDefault()), OAuth2Provider.LOCAL,adminRoles);
			User u3 = new User("Mateusz", "Kowalski", "modmateusz123", "mateusz@wp.pl", "mateusz", "123456789", true, LocalDateTime.now(ZoneId.systemDefault()), OAuth2Provider.LOCAL,modRoles);

			roleRepository.save(roleAdmin);
			roleRepository.save(roleModerator);
			roleRepository.save(roleUser);

			userRepository.save(u1);
			userRepository.save(u2);
			userRepository.save(u3);

//			offerTypeRepository.save(saleOfferType);
//			offerTypeRepository.save(rentOfferType);
//
//			offerStatusRepository.save(activeStatus);

//			offerRepository.save(offer);
//			offerRepository.save(offer2);
		};
	}
}
