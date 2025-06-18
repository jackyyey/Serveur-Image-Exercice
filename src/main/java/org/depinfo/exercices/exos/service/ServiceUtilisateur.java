package org.depinfo.exercices.exos.service;

import org.depinfo.exercices.exos.model.MUtilisateur;
import org.depinfo.exercices.exos.repository.DepotUtilisateur;
import org.kickmyb.transfer.RequeteInscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Component
public class ServiceUtilisateur implements UserDetailsService {

    public static class MauvaisNomOuMotDePasse extends RuntimeException {}
    public static class NomTropCourt extends RuntimeException {}
    public static class NomTropLong extends RuntimeException {}
    public static class NomDejaPris extends RuntimeException {}
    public static class MotDePasseTropCourt extends RuntimeException {}
    public static class MotDePasseTropLong extends RuntimeException {}
    public static class MotsDePasseDifferents extends RuntimeException {}

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired private DepotUtilisateur userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        MUtilisateur user = userRepository.findByNom(username.trim().toLowerCase()).get();
        return new User(user.nom, user.motDePasse, new ArrayList<>());
    }

    @Transactional(rollbackFor = NomDejaPris.class)
    public void inscrire(RequeteInscription req) {
        String username = req.nom.toLowerCase().trim();
        if (!req.motDePasse.equals(req.confirmationMotDePasse)) throw new MotsDePasseDifferents();
        if (username.length() < 2) throw new NomTropCourt();
        if (username.length() > 255) throw new NomTropLong();
        if (req.motDePasse.length() < 4) throw new MotDePasseTropCourt();
        if (req.motDePasse.length() > 255) throw new MotDePasseTropLong();
        try {
            MUtilisateur p = new MUtilisateur();
            p.nom = username;
            p.motDePasse = passwordEncoder.encode(req.motDePasse);
            userRepository.saveAndFlush(p);
        } catch (DataIntegrityViolationException e) {
            throw new NomDejaPris();
        }
    }
}