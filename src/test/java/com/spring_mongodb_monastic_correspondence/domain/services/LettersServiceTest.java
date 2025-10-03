package com.spring_mongodb_monastic_correspondence.domain.services;

import com.spring_mongodb_monastic_correspondence.application.services.CommentService;
import com.spring_mongodb_monastic_correspondence.application.services.LettersService;
import com.spring_mongodb_monastic_correspondence.application.services.VersionService;
import com.spring_mongodb_monastic_correspondence.infra.dtos.CreateLetterDTO;
import com.spring_mongodb_monastic_correspondence.infra.dtos.LetterWithCommentsDTO;
import com.spring_mongodb_monastic_correspondence.infra.dtos.LettersDTO;
import com.spring_mongodb_monastic_correspondence.infra.entities.LettersEntity;
import com.spring_mongodb_monastic_correspondence.infra.LettersMapper;
import com.spring_mongodb_monastic_correspondence.infra.repositories.LettersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.spring_mongodb_monastic_correspondence.domain.model.State.DEPRECATED;
import static com.spring_mongodb_monastic_correspondence.domain.model.State.READABLE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LettersServiceTest {

    @Mock
    LettersRepository lettersRepository;

    @InjectMocks
    LettersService lettersService;

    @Mock
    VersionService versionService;

    @Mock
    CommentService commentService;

    @Mock
    LettersMapper lettersMapper;

    private CreateLetterDTO createdLetter;

    private LettersEntity lettersEntity;

    private  LettersDTO expectedDTO;

    @BeforeEach
    public void setUp() {
        this.createdLetter = new CreateLetterDTO(
                "Father James", "Father Ramos",
                        "Hello Father Ramos, how are you doing?", 1850,
                        READABLE);

        this.lettersEntity = new LettersEntity (
                createdLetter.sender(), createdLetter.receiver(),
                createdLetter.content(), createdLetter.approximateYear(),
                createdLetter.currentState(), 0);

        this.expectedDTO = new LettersDTO (
                "123", // precisa definir um id, já que o banco de dados está sendo mockado
                createdLetter.sender(), createdLetter.receiver(),
                createdLetter.content(), createdLetter.approximateYear(),
                createdLetter.currentState(),0);

    }

    @Test
    public void shouldInsertLetter() {

        // dizer o que ele deve retornar quando save for chamado
        when(lettersRepository.save(any(LettersEntity.class))).thenReturn(lettersEntity);
        when(lettersMapper.toDTO(any(LettersEntity.class))).thenReturn(expectedDTO);

        LettersDTO result = lettersService.insertLetter(createdLetter);

        assertNotNull(result);
        assertEquals(expectedDTO, result);
        assertEquals(createdLetter.sender(), result.sender());

    }

    @Test
    void shouldDeleteLetterById() {
        when (lettersRepository.existsById("123")).thenReturn(true);
        var result = lettersService.deleteLetterById("123");
        assertTrue(result);
    }

    @Test
    void shouldUpdateLetterState() {

    }

    @Test
    void shouldUpdateContent() {
        //Arrange
        String newContent = "For testing";
        when(lettersRepository.findById("123")).thenReturn(Optional.ofNullable(this.lettersEntity));
        doNothing().when(versionService).saveOldVersion(any(LettersEntity.class));
        when(lettersRepository.save(any(LettersEntity.class))).thenReturn(this.lettersEntity);

        //Act
        lettersService.updateContent("123", newContent);

        // Test
        verify(versionService).saveOldVersion(lettersEntity); // Garante que chamou esse método

        assertEquals(newContent, lettersEntity.getContent());
        assertEquals(1, lettersEntity.getVersion());

    }

    @Test
    void shouldGetLetterById() {

        //Arrange
        when (lettersRepository.findById("123")).thenReturn(Optional.ofNullable(this.lettersEntity));

        //Act
        LetterWithCommentsDTO expectedLetter = new LetterWithCommentsDTO(
                createdLetter.sender(), createdLetter.receiver(),
                createdLetter.content(), createdLetter.approximateYear(),
                createdLetter.currentState(),0, List.of());

        var result = lettersService.getLetterById("123");

        //Assert
        assertEquals(expectedLetter, result);

    }

    @Test
    void shouldGetByKeyword() {

        // Arrange
        String searchContent = "Hello Father Ramos";

        List<LettersEntity> lettersWithContentEntity = new ArrayList<>();
        lettersWithContentEntity.add(this.lettersEntity);

        List<LettersDTO> lettersDTOS = new ArrayList<>();
        lettersDTOS.add(expectedDTO);

        when(lettersRepository.findByContentContainingIgnoreCase(searchContent)).thenReturn(lettersWithContentEntity);
        when(lettersMapper.toDTO(any(LettersEntity.class))).thenReturn(expectedDTO);


        //Act
        var result = lettersService.getByKeyword(searchContent);


        // Assert
        assertEquals(lettersDTOS, result);

    }

    @Test
    void shouldGetLettersFilteredNoDuplicates() {

        List<LettersEntity> entities = List.of(this.lettersEntity, this.lettersEntity);

        when(lettersRepository.findByCurrentState("READABLE")).thenReturn(entities);
        when(lettersMapper.toDTO(any(LettersEntity.class))).thenReturn(expectedDTO);


        var result = lettersService.getLettersFiltered("Father James", null, null, "READABLE");

        assertEquals(1, result.size());

    }

    @Test
    void shouldReturnEmptyList() {
        var result = lettersService.getLettersFiltered(null, null, null, null);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFixDeprecatedLetter() {

        String corruptedContent = "I'm f#ne, h=w @r& y=% d=#ng?";
        String contentFixed = "I'm fine, how are you doing?";

        LettersEntity corruptedLetter = new LettersEntity("456", "Father Ramos",
                "Father James", corruptedContent, 1851,
                DEPRECATED, 0);


        when(lettersRepository.findById("456")).thenReturn(Optional.of(corruptedLetter));
        when(lettersRepository.save(any(LettersEntity.class)))
                .thenReturn(corruptedLetter);


        String result = lettersService.fixLetter("456");

        assertEquals(contentFixed, result);
        assertEquals(1, corruptedLetter.getVersion());
        assertEquals(READABLE, corruptedLetter.getCurrentState());


    }
}