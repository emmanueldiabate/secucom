package com.GrafDigital.SecuCom.SecuCom.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity // Identifier comme une Entié JPA
@Getter // pour gerer les Getters
@Setter // pour gerer les Setters
@NoArgsConstructor // un constructeur sans argument
@AllArgsConstructor // un constructeur avec tous les arguments
public class AppUser {
    @Id // Identifier le Id;
    @GeneratedValue (strategy = GenerationType.IDENTITY) // Identifier notre Primary Key
    private Long id;

    @Column(length = 50)
    private String userName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Ignorer le password en Affichage
    private String password;

    @Column(length = 18)
    @ManyToMany(fetch = FetchType.EAGER) // Pour afficher le user avec son Rôle;
    private Collection<AppRole> appRoles = new ArrayList<>( );
}
