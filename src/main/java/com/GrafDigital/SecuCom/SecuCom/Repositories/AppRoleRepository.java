package com.GrafDigital.SecuCom.SecuCom.Repositories;

import com.GrafDigital.SecuCom.SecuCom.Models.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Toujours cette classe va étendre le l'interface JPA Repository<Entity,Long>
@Repository
public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
    // créons une méthode qui permet de retourner le Role de user pour l'authentification
    AppRole findByRoleName(String roleName);
}
