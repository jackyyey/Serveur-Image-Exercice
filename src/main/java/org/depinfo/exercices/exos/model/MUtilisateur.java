package org.depinfo.exercices.exos.model;

import jakarta.persistence.*;

@Entity
public class MUtilisateur {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Long id;

    @Column(unique = true)
    public String nom;

    @Basic
    public String motDePasse;
}