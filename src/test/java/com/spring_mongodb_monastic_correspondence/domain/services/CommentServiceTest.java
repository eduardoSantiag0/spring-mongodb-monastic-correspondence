package com.spring_mongodb_monastic_correspondence.domain.services;

import com.spring_mongodb_monastic_correspondence.application.exceptions.LetterNotFoundException;
import com.spring_mongodb_monastic_correspondence.application.services.CommentService;
import com.spring_mongodb_monastic_correspondence.domain.model.EmbeddedComment;
import com.spring_mongodb_monastic_correspondence.infra.dtos.CommentResponse;
import com.spring_mongodb_monastic_correspondence.infra.dtos.CreateCommentDTO;
import com.spring_mongodb_monastic_correspondence.infra.dtos.LetterWithCommentsDTO;
import com.spring_mongodb_monastic_correspondence.infra.entities.CommentDocument;
import com.spring_mongodb_monastic_correspondence.infra.entities.LettersEntity;
import com.spring_mongodb_monastic_correspondence.infra.repositories.CommentsRepository;
import com.spring_mongodb_monastic_correspondence.infra.repositories.LettersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.spring_mongodb_monastic_correspondence.domain.model.State.READABLE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentsRepository commentsRepository;

    @Mock
    private LettersRepository lettersRepository;

    private LettersEntity lettersEntity;
    private final int size = 5;
    private final int page = 5;
    private final String letterId = "1";

    Clock fixedClock = Clock.fixed(Instant.parse("2025-10-03T10:00:00Z"), ZoneId.systemDefault());


    @BeforeEach
    void setup() {
        Clock fixedClock = Clock.fixed(
                Instant.parse("2025-10-03T10:00:00Z"),
                ZoneId.systemDefault()
        );

        commentService.setClock(fixedClock);

        this.lettersEntity = new LettersEntity("1", "Father James", "Father Ramos",
                "Hello Father Ramos, how are you doing?", 1850,
                READABLE, 0);

    }

    @Test
    void addComment_shouldThrowLetterNotFoundException_whenLetterDoesNotExist() {
        assertThrows(LetterNotFoundException.class, () -> {
            commentService.addComment("99999",
                    new CreateCommentDTO("Father John Doe", "A comment"));
        });
    }

    @Test
    void addComment_shouldReturnResponseWithAuthorContentLetterIdAndVersion_whenLetterExists() {
        when(lettersRepository.findById("1")).thenReturn(Optional.ofNullable(lettersEntity));

        CommentResponse commentResponse = commentService.addComment("1",
                new CreateCommentDTO("Father John Doe", "A comment"));

        CommentResponse expected = new CommentResponse("1", "Father John Doe",
                "A comment", LocalDateTime.now(fixedClock), 0);

        assertEquals(expected, commentResponse);

    }

    @Test
    void addComment_shouldGenerateNonNullUUIDAndTimestampForCommentDocument(){
        //Arrange
        when(lettersRepository.findById(letterId)).thenReturn(Optional.of(lettersEntity));
        CreateCommentDTO comment1 = new CreateCommentDTO("Father Alan", "First comment");

        //Act
        commentService.addComment(letterId, comment1);
        EmbeddedComment embeddedComment  = lettersEntity.getLastComment();

        //Assert
        assertNotNull(embeddedComment);
        assertNotNull(embeddedComment.commentId());
        assertNotNull(embeddedComment.createdAt());
        assertDoesNotThrow(() -> UUID.fromString(String.valueOf(embeddedComment.commentId())));
    }

    @Test
    void addComment_shouldSaveUpdatedLetterAfterAddingEmbeddedComment(){

        //Arrange
        when(lettersRepository.findById(letterId)).thenReturn(Optional.of(lettersEntity));
        CreateCommentDTO comment1 = new CreateCommentDTO("Father Alan", "First comment");
        ArgumentCaptor<LettersEntity> captor = ArgumentCaptor.forClass(LettersEntity.class);

        //Act
        commentService.addComment(letterId, comment1);
        verify(lettersRepository).save(captor.capture());
        LettersEntity savedLetter = captor.getValue();

        // Assert
        assertNotNull(savedLetter.getLastComment());
        assertEquals("Father Alan", savedLetter.getLastComment().author());
        assertEquals("First comment", savedLetter.getLastComment().content());
        assertEquals(savedLetter.getLastComment().createdAt(), savedLetter.getLastCommentAt());

    }

    @Test
    void addComment_shouldUpdateLastCommentAndCountAndTimestamp() {

        Clock clock1 = Clock.fixed(Instant.parse("2025-10-03T10:00:00Z"), ZoneId.systemDefault());
        Clock clock2 = Clock.fixed(Instant.parse("2025-10-03T11:00:00Z"), ZoneId.systemDefault());

        when(lettersRepository.findById(letterId)).thenReturn(Optional.of(lettersEntity));

        // Arrange first comment
        commentService.setClock(clock1);
        CreateCommentDTO comment1 = new CreateCommentDTO("Father Alan", "First comment");
        commentService.addComment(letterId, comment1);

        EmbeddedComment lastCommentAfterFirst = lettersEntity.getLastComment();
        int count1 = lettersEntity.getCommentsCount();
        LocalDateTime timestamp1 = lettersEntity.getLastCommentAt();

        // Arrange second comment
        commentService.setClock(clock2);
        CreateCommentDTO comment2 = new CreateCommentDTO("Father Bob", "Second comment");
        commentService.addComment(letterId, comment2);

        EmbeddedComment lastCommentAfterSecond = lettersEntity.getLastComment();
        int count2 = lettersEntity.getCommentsCount();
        LocalDateTime timestamp2 = lettersEntity.getLastCommentAt();



        // Asserts
        assertNotEquals(lastCommentAfterFirst.author(), lastCommentAfterSecond.author());
        assertEquals(1, count1);
        assertEquals(2, count2);
        assertTrue(timestamp2.isAfter(timestamp1));

        verify(lettersRepository, times(2)).findById(letterId);
        verify(lettersRepository, times(2)).save(lettersEntity);
    }

    @Test
    void addComment_shouldAccumulateCountOverMultipleCalls() {
        when(lettersRepository.findById(letterId)).thenReturn(Optional.of(lettersEntity));
        CreateCommentDTO comment1 = new CreateCommentDTO("Father Alan", "First comment");
        CreateCommentDTO comment2 = new CreateCommentDTO("Father Bob", "Second comment");
        CreateCommentDTO comment3 = new CreateCommentDTO("Father Charles", "Third comment");

        commentService.addComment(letterId, comment1);
        commentService.addComment(letterId, comment2);
        commentService.addComment(letterId, comment3);

        EmbeddedComment lastComment = lettersEntity.getLastComment();
        assertEquals(3, lettersEntity.getCommentsCount());
        assertNotNull(lastComment);
        verify(lettersRepository, times(3)).findById(letterId);
    }

    @Test
    void addComment_shouldReplaceLastCommentWithMostRecent() {

        Clock clock1 = Clock.fixed(Instant.parse("2025-10-03T10:00:00Z"), ZoneId.systemDefault());
        Clock clock2 = Clock.fixed(Instant.parse("2025-10-03T11:00:00Z"), ZoneId.systemDefault());

        commentService.setClock(clock1);

        CreateCommentDTO firstComment = new CreateCommentDTO("Father Alan", "First comment");
        CreateCommentDTO secondComment= new CreateCommentDTO("Father Bob","Second comment");

        when(lettersRepository.findById(letterId)).thenReturn(Optional.ofNullable(lettersEntity));

        commentService.addComment(letterId, firstComment);
        LocalDateTime timestamp1 = lettersEntity.getLastCommentAt();

        EmbeddedComment lastCommentAfterFirst  = lettersEntity.getLastComment();

        commentService.setClock(clock2);
        commentService.addComment(letterId, secondComment);
        EmbeddedComment  lastCommentAfterSecond  = lettersEntity.getLastComment();
        LocalDateTime timestamp2 = lettersEntity.getLastCommentAt();


        assertEquals(lastCommentAfterSecond.createdAt(), lettersEntity.getLastCommentAt());
        assertTrue(timestamp2.isAfter(timestamp1));

        verify(lettersRepository, times(2)).findById(letterId);

    }


    @Test
    void searchCommentsInLetter_shouldCallRepositoryWithGivenIdPageAndSize() {

        //Arrange

        Pageable expectedPageable = PageRequest.of(page, size);

        when(commentsRepository.findByLetterId(eq(letterId), any(Pageable.class)))
                .thenReturn(Page.empty());


        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);

        //Act
        commentService.searchCommentsInLetter("1", page, size);
        verify(commentsRepository).findByLetterId(eq(letterId), captor.capture());
        Pageable captured = captor.getValue();

        //Assert
        assertEquals(expectedPageable.getPageNumber(), captured.getPageNumber());
        assertEquals(expectedPageable.getPageSize(), captured.getPageSize());

    }

    @Test
    void searchCommentsInLetter_shouldReturnEmptyPage_whenRepositoryReturnsEmpty() {
        String nonExistingLetterId = "2";

        when(commentsRepository.findByLetterId(eq(nonExistingLetterId), any(Pageable.class)))
                .thenReturn(Page.empty());

        Page<CommentResponse> comments = commentService.searchCommentsInLetter(nonExistingLetterId, page, size);

        assertNotNull(comments);
        assertTrue(comments.isEmpty());
        verify(commentsRepository).findByLetterId(eq(nonExistingLetterId), any(Pageable.class));

    }

    @Test
    void getAllComments_shouldMapAllCommentDocumentsToResponses() {

        //Arrange

        CommentDocument comment1 = new CommentDocument(
                UUID.randomUUID(),
                letterId,
                "Father Alan",
                "First comment",
                LocalDateTime.now(),
                1
        );

        CommentDocument comment2 = new CommentDocument(
                UUID.randomUUID(),
                letterId,
                "Father Bob",
                "Second Commnet",
                LocalDateTime.now(),
                2
        );

        List<CommentDocument> commentDocuments = List.of(comment1, comment2);

        when(commentsRepository.findByLetterId(letterId)).thenReturn(commentDocuments);

        //Act
        List<CommentResponse> responses = commentService.getAllComments(letterId);

        //Assert
        CommentResponse response1 = responses.get(0);
        assertEquals(comment1.getLetterId(), response1.letterId());
        assertEquals(comment1.getAuthor(), response1.author());
        assertEquals(comment1.getContent(), response1.content());
        assertEquals(comment1.getCreatedAt(), response1.createdAt());
        assertEquals(comment1.getVersionCommented(), response1.versionCommented());

        CommentResponse response2 = responses.get(1);
        assertEquals(comment2.getLetterId(), response2.letterId());
        assertEquals(comment2.getAuthor(), response2.author());
        assertEquals(comment2.getContent(), response2.content());
        assertEquals(comment2.getCreatedAt(), response2.createdAt());
        assertEquals(comment2.getVersionCommented(), response2.versionCommented());

        verify(commentsRepository).findByLetterId(letterId);
    }

    @Test
    void getAllComments_shouldReturnEmptyList_whenNoCommentsForLetter() {

        when(commentsRepository.findByLetterId(letterId)).thenReturn(List.of());

        List<CommentResponse> result = commentService.getAllComments(letterId);

        assertEquals(List.of(), result);
        assertNotNull(result);
        verify(commentsRepository).findByLetterId(letterId);
    }



}