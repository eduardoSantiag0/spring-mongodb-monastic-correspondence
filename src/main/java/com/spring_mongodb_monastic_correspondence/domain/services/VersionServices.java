package com.spring_mongodb_monastic_correspondence.domain.services;

import com.spring_mongodb_monastic_correspondence.domain.model.LetterVersion;
import com.spring_mongodb_monastic_correspondence.domain.model.LettersEntity;
import com.spring_mongodb_monastic_correspondence.infra.repositories.LetterVersionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VersionServices {

    @Autowired
    private LetterVersionsRepository versionsRepository;

    public void saveOldVersion(LettersEntity oldVersion) {
        LetterVersion toSave = new LetterVersion(oldVersion);
        versionsRepository.save(toSave);
    }


    public List<LetterVersion> getVersionsOfLetter(String originalId) {
        return versionsRepository.findByOriginalId(originalId);
    }
}
