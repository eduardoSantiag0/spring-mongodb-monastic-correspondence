package com.spring_mongodb_monastic_correspondence.infra.repositories;

import com.spring_mongodb_monastic_correspondence.domain.model.LettersEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface LettersRepository extends MongoRepository<LettersEntity, String> {
    List<LettersEntity> findBySenderOrReceiver(String sender, String receiver);
    List<LettersEntity> findByCurrentState(String state);
    List<LettersEntity> findByApproximateYear(Integer date);
    List<LettersEntity> findByContentContainingIgnoreCase(String keyword);
}
