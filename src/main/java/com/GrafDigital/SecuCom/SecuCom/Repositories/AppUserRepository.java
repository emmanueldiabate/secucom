package com.GrafDigital.SecuCom.SecuCom.Repositories;
// cette classe va étendre la l'interface JPA Repository avec <Entity,ID>

import com.GrafDigital.SecuCom.SecuCom.Models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    // créons une méthode qui permet de retourner le AppUser pour l'authentification
    AppUser findByUserName (String userName);
}
