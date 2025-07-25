package com.spring_mongodb_monastic_correspondence.application;


import com.spring_mongodb_monastic_correspondence.domain.model.LetterVersion;
import com.spring_mongodb_monastic_correspondence.domain.services.VersionServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/versions")
public class LettersVersionsController {

    @Autowired
    private VersionServices versionService;

    // Listar todas as versões com filtros opcionais

    // getByOriginalId > listar todas as versões de uma carta
     @GetMapping("/of/{originalId}")
    public ResponseEntity<List<LetterVersion>> getVersionsOfLetter(@PathVariable String originalId) {
         List<LetterVersion> versions = versionService.getVersionsOfLetter(originalId);
         return ResponseEntity.ok(versions);
     }

     //todo Está retornando vetor vazio
    // todo Está fazendo update mas não está jogando no repositorio correto

}
