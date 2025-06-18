package org.depinfo.exercices.exos.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.depinfo.exercices.exos.service.ServiceUtilisateur;
import org.kickmyb.transfer.ReponseConnexion;
import org.kickmyb.transfer.RequeteConnexion;
import org.kickmyb.transfer.RequeteInscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private ServiceUtilisateur serviceUtilisateur;

    private @Autowired HttpServletRequest request;
    private @Autowired HttpServletResponse response;
    private @Autowired SecurityContextRepository securityContextRepository;

    @PostMapping("/id/connexion")
    public @ResponseBody ReponseConnexion connexion(@RequestBody RequeteConnexion s) {
        System.out.println("ID : Demande connexion " + s.nom);
        s.nom = s.nom.trim().toLowerCase();
        try {
            Authentication auth = new UsernamePasswordAuthenticationToken(s.nom, s.motDePasse);
            auth = authManager.authenticate(auth);
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
            securityContextRepository.saveContext(context, request, response);
            System.out.println("Logged as " + s.nom);
            ReponseConnexion resp = new ReponseConnexion();
            resp.nomUtilisateur = s.nom;
            return resp;
        } catch (org.springframework.security.authentication.BadCredentialsException bce) {
            throw new ServiceUtilisateur.MauvaisNomOuMotDePasse();
        }
    }

    @PostMapping("/id/inscription")
    public @ResponseBody ReponseConnexion inscription(@RequestBody RequeteInscription s) {
        System.out.println("ID : demande connexion " + s.nom);
        serviceUtilisateur.inscrire(s);
        RequeteConnexion req = new RequeteConnexion();
        req.nom = s.nom;
        req.motDePasse = s.motDePasse;
        return connexion(req);
    }

    @PostMapping(value = "/id/deconnexion", produces = "plain/text")
    public @ResponseBody String deconnexion() {
        System.out.println("ID : Demande déconnexion ");
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(null);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);
        return "";
    }
}