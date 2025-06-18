package org.depinfo.exercices.exos.repository;

import org.depinfo.exercices.exos.model.MUtilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepotUtilisateur extends JpaRepository<MUtilisateur, Long> {


    Optional<MUtilisateur> findByNom(String nomUtilisateur);
}