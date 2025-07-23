package com.spring_mongodb_monastic_correspondence.infra;

import com.spring_mongodb_monastic_correspondence.domain.model.LettersEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LettersRepository extends MongoRepository<LettersEntity, String> {
    List<LettersEntity> findBySenderOrReceiver(String sender, String receiver);
    List<LettersEntity> findByCurrentState(String state);

    List<LettersEntity> findByApproximateYear(Integer date);

    @Query("SELECT l FROM LettersEntity l WHERE LOWER(l.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<LettersEntity>findByContent(String keyword);

    List<LettersEntity> findByContentContainingIgnoreCase(String keyword);
}
