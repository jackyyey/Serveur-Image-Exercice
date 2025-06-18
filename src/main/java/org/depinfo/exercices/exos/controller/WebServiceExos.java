package org.depinfo.exercices.exos.controller;

import jakarta.servlet.http.HttpSession;
import org.depinfo.exercices.exos.model.*;
import org.depinfo.exercices.exos.service.ServicePhoto;
import org.imgscalr.Scalr;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Ceci est un web service pour pratiquer les étudiants sur un exemple déployé
 * Pratiquer l'envoi et la réception de
 * - Valeur simple en envoi et retour
 * - Valeur complexe et JSON / GSON
 * - Liste de valeurs complexes
 * - Valeur d'erreur selon l'objet envoyé en requête
 */

@Controller
public class WebServiceExos {

    @Autowired
    private ServicePhoto servicePhoto;

    @GetMapping(value = "/exos/waitaminute", produces = "text/plain")
    public @ResponseBody String attendUnPeu() throws InterruptedException {
        Thread.sleep(5000);
        return "TADA";
    }

    // Simple valeur en arrivée et sortie
    @PostMapping("/exos/flottant/double")
    public @ResponseBody Float addOne(@RequestBody Float valeur) {
        return 2 * valeur;
    }

    @GetMapping("/exos/long/double/{valeur}")
    public @ResponseBody Long addOne(@PathVariable long valeur) {
        return 2 * valeur;
    }

    @GetMapping(value = "/exos/truc/complexe", produces = "application/json")
    public @ResponseBody Truc getFor(@RequestParam("name") String name) {
        System.out.println("WS EXOS : get a truc for " + name);
        Truc t = new Truc();
        t.b = name;
        t.a = new Random().nextInt(10);
        for (long n = 0; n < t.a; n++) {
            t.c.add(n);
        }
        return t;
    }


    @PostMapping(value = "/exos/truc/doubler", produces = "application/json")
    public @ResponseBody Truc doubleLeTruc(@RequestBody Truc truc) {
        System.out.println("WS EXOS : un truc a double " + truc.b);
        Truc t = new Truc();
        t.b = truc.b;
        t.a = truc.a * 2;
        for (long n : truc.c) {
            t.c.add(n * 2);
        }
        return t;
    }

    @GetMapping(value = "/exos/long/list", produces = "application/json")
    public @ResponseBody List<Long> getListeLong() {
        System.out.println("WS SOCIAL : list of trucs");
        List<Long> res = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {

            res.add(Long.MAX_VALUE - i);
        }
        return res;
    }

    @PostMapping(value = "/exos/date", produces = "application/json")
    public @ResponseBody TrucAvecUneDate date(@RequestBody TrucAvecUneDate valeur) {
        System.out.println("Truc avec une date " + valeur.date.toString());
        if (valeur == null) {
            return new TrucAvecUneDate();
        }
        DateTime dd = new DateTime(valeur.date);
        valeur.date = dd.plusDays(7).toDate();
        return valeur;
    }

    @GetMapping("/exos/image")
    public ResponseEntity<byte[]> image(@RequestParam(value = "width", required = false) Integer maxWidth) throws IOException {
        System.out.println("PHOTO : download request width " + maxWidth);
        byte[] bytes = this.getClass().getClassLoader().getResourceAsStream("large.jpg").readAllBytes();
        if (maxWidth == null) { // no resizing
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
        } else {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            BufferedImage bi = ImageIO.read(bais);
            BufferedImage resized = Scalr.resize(bi, Scalr.Method.SPEED, maxWidth);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resized, "jpg", baos);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(baos.toByteArray());
        }
    }

    @PostMapping(value = "/exos/fileasmultipart", produces = "text/plain")
    public ResponseEntity<String> up(@RequestParam("file") MultipartFile file, @RequestParam("babyID") Long babyID) throws IOException {
        System.out.println("PHOTO : upload request " + file.getContentType());
        return ResponseEntity.status(HttpStatus.OK).body("taille " + file.getBytes().length);
    }

    @PostMapping(value = "/file", produces = "text/plain")
    public ResponseEntity<String> storePhoto(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("PHOTO : upload request " + file.getContentType());
        MPhoto p = servicePhoto.store(file);
        return ResponseEntity.status(HttpStatus.OK).body(p.id.toString());
    }

    @GetMapping("/file/{id}")
    public ResponseEntity<byte[]> taskPhoto(@PathVariable Long id, @RequestParam(required = false, name = "width") Integer maxWidth) throws IOException {
        System.out.println("PHOTO : download request " + id + " width " + maxWidth);
        MPhoto pic = servicePhoto.getFile(id);
        if (maxWidth == null) { // no resizing
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(pic.blob);
        } else {
            ByteArrayInputStream bais = new ByteArrayInputStream(pic.blob);
            BufferedImage bi = ImageIO.read(bais);
            BufferedImage resized = Scalr.resize(bi, Scalr.Method.ULTRA_QUALITY, maxWidth);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resized, "jpg", baos);
            byte[] bytes = baos.toByteArray();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
        }
    }

    @GetMapping(value = "/exos/truc/list")
    public @ResponseBody List<Truc> getListe() throws InterruptedException {
        System.out.println("WS SOCIAL : list of trucs");
        Thread.sleep(2500);
        List<Truc> res = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Truc t = new Truc();
            t.b = "Pipo" + i;
            t.a = new Random().nextInt(10);
            for (long n = 0; n < t.a; n++) {
                t.c.add(new Random().nextLong());
            }
            res.add(t);
        }
        return res;
    }

    // TODO error based on value sent
    @PostMapping(value = "/exos/error/or/not/", produces = "text/plain")
    public @ResponseBody String errorOrNot(@RequestBody Requete req) throws TropCourt {
        if (req.nom.length() < 5) throw new TropCourt();
        return "Yeah!!!";
    }

    @GetMapping(value = "/exos/cookie/echo", produces = "text/plain")
    public @ResponseBody String echoCookie(HttpSession session) {
        return session.getId();
    }

    @GetMapping(value = "/exos/chic/type/")
    public @ResponseBody String chicType() {
        return new Random().nextInt() + "";
    }

}
