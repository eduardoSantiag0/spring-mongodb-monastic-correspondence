package com.spring_mongodb_monastic_correspondence.domain.services;

import com.spring_mongodb_monastic_correspondence.domain.dtos.LetterVersionsDTO;
import com.spring_mongodb_monastic_correspondence.domain.model.LetterVersion;
import com.spring_mongodb_monastic_correspondence.domain.model.LettersEntity;
import com.spring_mongodb_monastic_correspondence.infra.repositories.LetterVersionsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static com.spring_mongodb_monastic_correspondence.domain.model.State.IN_REVIEW;
import static com.spring_mongodb_monastic_correspondence.domain.model.State.READABLE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VersionServicesTest {

    @Mock
    LetterVersionsRepository versionsRepository;

    @InjectMocks
    VersionServices versionServices;

    private LettersEntity entity1;

    private LettersEntity entity2;

    private LetterVersionsDTO versionDTO;

    private LetterVersion version1;

    private LetterVersion version2;

    @BeforeEach
    void setUp() {
        this.entity1 = new LettersEntity(
                "789",
                "Father James", "Father Ramos",
                "Hello Father Ramos, how are you doing?", 1850,
                IN_REVIEW, 0);

        this.entity2 = new LettersEntity(
                "789",
                "Father James", "Father Ramos",
                "Hello Father Ramos, how are you doing?", 1850,
                READABLE, 1);

        this.version1 = new LetterVersion(entity1);
        this.version2 = new LetterVersion(entity2);
    }


    @Test
    void shouldGetVersionsOfLetter() {

        //Arrange
        when(versionsRepository.findByOriginalId("789"))
                .thenReturn(List.of(version1, version2));
        // Act
        List<LetterVersionsDTO> result = versionServices.getVersionsOfLetter("789");

        // Assert
        assertEquals(2, result.size());
        assertEquals(entity1.getContent(), result.get(0).content());
        assertEquals(entity2.getContent(), result.get(1).content());
        verify(versionsRepository).findByOriginalId("789");
    }

    @Test
    void shouldReturnSameLetterWithDifferentVersion() {
        // Arrange
        when(versionsRepository.findByOriginalId("789"))
                .thenReturn(List.of(version1, version2));

        // Act
        List<LetterVersionsDTO> result = versionServices.getVersionsOfLetter("789");

        // Assert
        assertEquals(2, result.size());
        assertNotEquals(result.get(0).version(), result.get(1).version());
    }

    @Test
    void shouldReturnAllVersionOfTheLetter() {
        // Arrange
        when(versionsRepository.findByOriginalId("789"))
                .thenReturn(List.of(version1, version2));

        // Act
        List<LetterVersionsDTO> result = versionServices.getVersionsOfLetter("789");

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(v -> v.content().equals(entity1.getContent())));
        assertTrue(result.stream().anyMatch(v -> v.content().equals(entity2.getContent())));
    }

    @Test
    void shouldReturnEmptyListIfNotFound() {
        // Arrange
        when(versionsRepository.findByOriginalId("999"))
                .thenReturn(Collections.emptyList());

        // Act
        List<LetterVersionsDTO> result = versionServices.getVersionsOfLetter("999");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(versionsRepository).findByOriginalId("999");
    }


}