package com.spring_mongodb_monastic_correspondence.infra.repositories;

import com.spring_mongodb_monastic_correspondence.infra.entities.CommentDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CommentsRepository extends
        PagingAndSortingRepository <CommentDocument, String>,
        MongoRepository<CommentDocument, String>
{
    Page<CommentDocument> findByLetterId(String letterId, Pageable pageable);

    List<CommentDocument> findByLetterId(String id);
}
