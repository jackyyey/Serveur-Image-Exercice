package org.depinfo.exercices.exos.service;

import org.depinfo.exercices.exos.model.MPhoto;
import org.depinfo.exercices.exos.repository.DepotPhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ServicePhoto {

    @Autowired
    private DepotPhoto depotPhoto;

    @Transactional
    public MPhoto store(MultipartFile fichier) throws IOException {
        MPhoto photo = new MPhoto();
        photo.blob = fichier.getBytes();
        photo.typeContenu = fichier.getContentType();
        photo = depotPhoto.save(photo);
        return photo;
    }

    public MPhoto getFile(Long id) {
        return depotPhoto.findById(id).get();
    }
}