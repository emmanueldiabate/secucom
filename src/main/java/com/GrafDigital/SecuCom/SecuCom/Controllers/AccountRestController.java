package com.GrafDigital.SecuCom.SecuCom.Controllers;

import com.GrafDigital.SecuCom.SecuCom.Models.AppRole;
import com.GrafDigital.SecuCom.SecuCom.Models.AppUser;
import com.GrafDigital.SecuCom.SecuCom.Services.AccountService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController // Identifier la classe comme Controllers
@AllArgsConstructor // Pour l'injections des dependances, AccountService;
@RequestMapping("/SecuCom")
public class AccountRestController {
    // Injectons la dependance
    public final AccountService accountService;

    // Une méthode qui permet de retourner une liste des Users
    @GetMapping("/users")
    public List<AppUser> appUsers(){
        return accountService.listUsers();
    }

    // Une méthode qui permet d'ajouter un User
    @PostMapping("/AddUser")
    public AppUser saveUser(@RequestBody AppUser appUser){ // @RequestBody pour prendre les données de qui se trouve dans le Body
        return accountService.addNewUser(appUser);
    }

    // Une méthode qui permet d'ajouter un Rôle
    @PostMapping("/addRole")
    public AppRole saveRole(@RequestBody AppRole appRole){
        return accountService.addNewRole(appRole);
    }

    // Une méthode qui permet d'Affecter un rôle à un User
    @PostMapping("/addRoleToUser")
    public void addRoleToUser(@RequestBody RoleUserForm roleUserForm){
         accountService.addRoleToUser(roleUserForm.getUserName(), roleUserForm.getRoleName());
    }

    // Méthode Delete
    @DeleteMapping("/delete/{IdAppUser}")
    public String delete(@PathVariable Long IdAppUser){
        return accountService.supprimer(IdAppUser);
    }


}

@Getter
@Setter
class RoleUserForm{
    private String userName;
    private String roleName;
}
