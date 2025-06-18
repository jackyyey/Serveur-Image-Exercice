package org.depinfo.exercices.exos.controller;

import org.depinfo.exercices.exos.model.MPhoto;
import org.depinfo.exercices.exos.service.ServicePhoto;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
public class ImageController {

    @Autowired
    private ServicePhoto servicePhoto;

    @PostMapping(value = "/exos/fileasmultipart", produces = "text/plain")
    public ResponseEntity<String> up(@RequestParam("file") MultipartFile file, @RequestParam("babyID") Long babyID) throws IOException {
        System.out.println("PHOTO : upload request " + file.getContentType());
        return ResponseEntity.status(HttpStatus.OK).body("taille " + file.getBytes().length);
    }

    @PostMapping(value = "/exos/file", produces = "text/plain")
    public ResponseEntity<String> storePhoto(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("PHOTO : upload request " + file.getContentType());
        MPhoto p = servicePhoto.store(file);
        return ResponseEntity.status(HttpStatus.OK).body(p.id.toString());
    }

    @GetMapping("/exos/file/{id}")
    public ResponseEntity<byte[]> taskPhoto(@PathVariable Long id, @RequestParam(required = false, name = "width") Integer maxWidth) throws IOException {
        System.out.println("PHOTO : download request " + id + " width " + maxWidth);
        MPhoto pic = servicePhoto.getFile(id);
        if (maxWidth == null) {
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

    @PostMapping("/exos/authed/file")
    public ResponseEntity<String> upSingleCookie(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("PHOTO : cookie " + file.getContentType());
        return storePhoto(file);
    }

    @GetMapping("/exos/authed/file/{id}")
    public ResponseEntity<byte[]> photoSingleCookie(@PathVariable Long id, @RequestParam(required = false, name = "width") Integer maxWidth) throws IOException {
        return taskPhoto(id, maxWidth);
    }

    @GetMapping("/exos/image")
    public ResponseEntity<byte[]> image(@RequestParam(value = "width", required = false) Integer maxWidth) throws IOException {
        System.out.println("PHOTO : download request width " + maxWidth);
        byte[] bytes = this.getClass().getClassLoader().getResourceAsStream("large.jpg").readAllBytes();
        if (maxWidth == null) {
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
}
