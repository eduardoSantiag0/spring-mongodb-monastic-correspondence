package com.spring_mongodb_monastic_correspondence.application.controller;


import com.spring_mongodb_monastic_correspondence.infra.dtos.LetterVersionsDTO;
import com.spring_mongodb_monastic_correspondence.application.services.VersionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/versions")
public class LetterVersionsController {

    @Autowired
    private VersionService versionService;


     @GetMapping("/of/{originalId}")
     @Operation(summary = "Versions of the same letter", description = "Returns a list of changes in the same letter.")
     @ApiResponse(responseCode = "200", description = "List of letters returned")
    public ResponseEntity<List<LetterVersionsDTO>> getVersionsOfLetter(@PathVariable String originalId) {
         List<LetterVersionsDTO> versions = versionService.getVersionsOfLetter(originalId);
         return ResponseEntity.ok(versions);
     }

}
