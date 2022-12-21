package com.GrafDigital.SecuCom.SecuCom.Services;

import com.GrafDigital.SecuCom.SecuCom.Models.AppRole;
import com.GrafDigital.SecuCom.SecuCom.Models.AppUser;

import java.util.List;

public interface AccountService {
    // Une méthode qui permet d'ajouter un User
    AppUser addNewUser(AppUser appUser);

    // Une méthode qui permet d'ajouter un Rôle;
    AppRole addNewRole (AppRole appRole);

    // une méthode pour affecter un Rôle à un User
    void addRoleToUser(String userName, String roleName);

    // Une méthode qui permet de retourner un User
    AppUser loadUserByUserName(String userName);

    // une méthode qui permet d'afficher la liste des Users
    List<AppUser> listUsers();

    // Une méthode va retourné String qui va prendre en param (IdAppUser) à supprimer;
    String supprimer(Long idAppUser);
}
