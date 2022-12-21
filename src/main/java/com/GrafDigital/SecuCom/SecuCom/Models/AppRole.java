package com.GrafDigital.SecuCom.SecuCom.Models;

import lombok.*;

import javax.persistence.*;

@Entity // Définis la classe comme une entité JPA;
@Getter // Générer les Getters
@Setter // Générer les Setters
@NoArgsConstructor
@AllArgsConstructor
public class AppRole {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String roleName;
}
