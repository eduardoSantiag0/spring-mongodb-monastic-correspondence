package com.spring_mongodb_monastic_correspondence.infra.repositories;

import com.spring_mongodb_monastic_correspondence.infra.entities.LettersEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LettersRepository extends MongoRepository<LettersEntity, String> {
    List<LettersEntity> findBySenderOrReceiver(String sender, String receiver);
    List<LettersEntity> findByCurrentState(String state);
    List<LettersEntity> findByApproximateYear(Integer date);
    List<LettersEntity> findByContentContainingIgnoreCase(String keyword);
}
