package org.depinfo.exercices.exos.model;

import jakarta.persistence.*;

@Entity
public class MPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Lob
    public byte[] blob;
    @Basic  public String typeContenu;
}