package com.spring_mongodb_monastic_correspondence.application.services;

import com.spring_mongodb_monastic_correspondence.application.exceptions.LetterNotFoundException;
import com.spring_mongodb_monastic_correspondence.infra.entities.CommentDocument;
import com.spring_mongodb_monastic_correspondence.domain.model.EmbeddedComment;
import com.spring_mongodb_monastic_correspondence.infra.entities.LettersEntity;
import com.spring_mongodb_monastic_correspondence.infra.dtos.CommentResponse;
import com.spring_mongodb_monastic_correspondence.infra.dtos.CreateCommentDTO;
import com.spring_mongodb_monastic_correspondence.infra.repositories.CommentsRepository;
import com.spring_mongodb_monastic_correspondence.infra.repositories.LettersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class CommentService {

    private final CommentsRepository commentsRepository;

    private final LettersRepository lettersRepository;

    private Clock clock = Clock.systemDefaultZone();

    public CommentService(CommentsRepository commentsRepository, LettersRepository lettersRepository) {
        this.commentsRepository = commentsRepository;
        this.lettersRepository = lettersRepository;
    }



    public CommentResponse addComment(String id, CreateCommentDTO dto) {

        LettersEntity letter = lettersRepository.findById(String.valueOf(id))
                .orElseThrow(() -> new LetterNotFoundException("Letter not found with id: " + id));

        LocalDateTime now = LocalDateTime.now(clock);


        CommentDocument newComment = new CommentDocument(UUID.randomUUID(), id, dto.author(), dto.content(),
                now, letter.getVersion());

        commentsRepository.save(newComment);

        EmbeddedComment embeddedComment = new EmbeddedComment(newComment.getCommentId()
                , newComment.getAuthor(), newComment.getContent(),
                newComment.getCreatedAt(), newComment.getVersionCommented());

        // Atualizar embeddeds
        letter.addComment(embeddedComment);
        lettersRepository.save(letter);

        log.info(
                "ACTION=CREATE COMMENT_ID={} LETTER_ID={} AUTHOR={} VERSION={} TIMESTAMP={}",
                newComment.getCommentId(),
                newComment.getLetterId(),
                newComment.getAuthor(),
                newComment.getVersionCommented(),
                newComment.getCreatedAt()
        );


        return new CommentResponse(id, dto.author(), dto.content(), now, letter.getVersion());

    }

    public Page<CommentResponse> searchCommentsInLetter(String id, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        var comments = commentsRepository.findByLetterId(id,pageable);

        return comments.map(comment -> new CommentResponse(
                comment.getLetterId(),
                comment.getAuthor(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getVersionCommented()
        ));
    }

    public List<CommentResponse> getAllComments(String id) {
        var commentsEntity = commentsRepository.findByLetterId(id);

        return commentsEntity.stream().map(c ->
                new CommentResponse(
                        c.getLetterId(),
                        c.getAuthor(),
                        c.getContent(),
                        c.getCreatedAt(),
                        c.getVersionCommented()
                )).toList();
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }
}
