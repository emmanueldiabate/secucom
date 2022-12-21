package com.GrafDigital.SecuCom.SecuCom;

import com.GrafDigital.SecuCom.SecuCom.Models.AppRole;
import com.GrafDigital.SecuCom.SecuCom.Models.AppUser;
import com.GrafDigital.SecuCom.SecuCom.Repositories.AppRoleRepository;
import com.GrafDigital.SecuCom.SecuCom.Repositories.AppUserRepository;
import com.GrafDigital.SecuCom.SecuCom.Services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
@AllArgsConstructor
public class SecuComAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecuComAppApplication.class, args);
	}
	// Encoder le mots de passe;
	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	// Pour l'injection de notre Repository Role pour vérifier la table
	private final AppRoleRepository appRoleRepository;

	// Pour l'injection de notre Repository AppUser pour vérifier la table
	private final AppUserRepository appUserRepository;

	// Au démmarage de l'application
	@Bean // Annotation Bean pour que ça exécute, // Donnons notre interface serive en paramettre
	CommandLineRunner start(AccountService accountService){
		return  args -> {
			// Vérifié si le la table Role est vide;
			if (appRoleRepository.findAll().size() == 0) { // Ajoutons des Rôles
				accountService.addNewRole(new AppRole(null, "USER"));
				accountService.addNewRole(new AppRole(null, "ADMIN"));
			}

			if (appUserRepository.findAll().size() == 0) {
				// Ajouter un Admin par défaut
				accountService.addNewUser(new AppUser(null, "Jeremi Koloma", "123", new ArrayList<>()));

				// Attribuer des roles à l'Admin
				accountService.addRoleToUser("Jeremi Koloma", "USER");
				accountService.addRoleToUser("Jeremi Koloma", "ADMIN");
			}
		};
	}

}
