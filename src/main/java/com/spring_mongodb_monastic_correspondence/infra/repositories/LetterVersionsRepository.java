package com.spring_mongodb_monastic_correspondence.infra.repositories;

import com.spring_mongodb_monastic_correspondence.domain.model.LetterVersion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LetterVersionsRepository extends MongoRepository<LetterVersion, String> {
    List<LetterVersion> findByOriginalId (String originalId);
}
