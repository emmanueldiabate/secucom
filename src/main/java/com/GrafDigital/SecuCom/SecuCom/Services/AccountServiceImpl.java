package com.GrafDigital.SecuCom.SecuCom.Services;

// cette classe va Implementer notre interface AccountService pour les méthodes

import com.GrafDigital.SecuCom.SecuCom.Models.AppRole;
import com.GrafDigital.SecuCom.SecuCom.Models.AppUser;
import com.GrafDigital.SecuCom.SecuCom.Repositories.AppRoleRepository;
import com.GrafDigital.SecuCom.SecuCom.Repositories.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service // Identifions la classe comme service
@Transactional
@AllArgsConstructor // Pour l'injections des dépendances de nos Respository;
public class AccountServiceImpl implements AccountService {
    // Injectons maintenant les dependances Repositories;
    private final AppUserRepository appUserRepository;
    private final AppRoleRepository appRoleRepository;

    // Déclarer le passwordEncoder pour pouvoir l'utiliser
    private PasswordEncoder passwordEncoder;

    // On implemente nos méthodes
    @Override // Ajouter un User
    public AppUser addNewUser(AppUser appUser) {
        // Encodons le password
        String pwd = appUser.getPassword(); // recupérer le password
        appUser.setPassword(passwordEncoder.encode(pwd)); // Encoder le password
        return appUserRepository.save(appUser); // Enregister user pour la persitance des données;
    }

    @Override // Ajouter un Rôle
    public AppRole addNewRole(AppRole appRole) {
        return appRoleRepository.save(appRole); // Enregister le Role pour la persitance des données;
    }

    @Override // Affecter un Rôle à un User
    public void addRoleToUser(String userName, String roleName) {
        // récupérer d'abord le User dans la Base de Donnée;
        AppUser appUser = appUserRepository.findByUserName(userName);

        // récupérons aussi le Rôle dans la Base de Donnée;
        AppRole appRole = appRoleRepository.findByRoleName(roleName);

        // Maintenant ajoutons le Rôle à user
        appUser.getAppRoles().add(appRole);
    }

    @Override // charger un User par son Nom
    public AppUser loadUserByUserName(String userName) {
        return appUserRepository.findByUserName(userName);
    }

    @Override // Retourner la liste des Users
    public List<AppUser> listUsers() {
        return appUserRepository.findAll();
    }

    @Override // Supprimer un User
    public String supprimer(Long idAppUser) {
        AppUser userasupprimer = appUserRepository.findById(idAppUser).orElse(null);
        if (userasupprimer != null) { // Vérifié si le User Existe
            appUserRepository.deleteById(idAppUser);
            return "Collaborateur Supprimer !";
        }
        else {  // Sinon S'il n'Existe
            return "Ce collaborateur n'existe pas !";
        }

    }
}
