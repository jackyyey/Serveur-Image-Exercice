package org.depinfo.exercices.exos.repository;

import org.depinfo.exercices.exos.model.MPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepotPhoto extends JpaRepository<MPhoto, Long> {

}
