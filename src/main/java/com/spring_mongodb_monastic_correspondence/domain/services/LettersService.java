package com.spring_mongodb_monastic_correspondence.domain.services;

import com.spring_mongodb_monastic_correspondence.application.exceptions.InvalidStateExeption;
import com.spring_mongodb_monastic_correspondence.application.exceptions.LetterNotFoundException;
import com.spring_mongodb_monastic_correspondence.domain.dtos.LettersDTO;
import com.spring_mongodb_monastic_correspondence.domain.model.LettersEntity;
import com.spring_mongodb_monastic_correspondence.domain.model.State;
import com.spring_mongodb_monastic_correspondence.infra.LettersMapper;
import com.spring_mongodb_monastic_correspondence.infra.repositories.LettersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class LettersService {

    @Autowired
    private LettersRepository lettersRepository;

    @Autowired
    private VersionServices versionServices;

    @Autowired
    private LettersMapper mapper;


    private String simulateDeprecation(String data) {
        return data
                .replace("a", "@")
                .replace("e", "&")
                .replace("i", "#")
                .replace("o", "=")
                .replace("u", "%");
    }

    private String simulateFixingContent (String data) {
        return data
                .replace("@", "a")
                .replace("&", "e")
                .replace("#", "i")
                .replace("=", "o")
                .replace("%", "u");
    }


    public LettersDTO insertLetter(LettersDTO dto) {

        String content = dto.content();

        if (dto.currentState() == State.DEPRECATED) {
            content = simulateDeprecation(content);
        }

        LettersEntity entity = new LettersEntity(dto.sender(), dto.receiver(), content, dto.approximateYear(), dto.currentState(), 0);

        lettersRepository.save(entity);

//        log.info("ACTION=CREATE ID={} AUTHOR={}", entity.getId(), entity.getSender());
        log.info("No ano {}, o irmão {} mandou uma carta para o irmão {}. A carta foi encontrada no estado {}",
                entity.getApproximateYear(), entity.getSender(), entity.getReceiver(), entity.getCurrentState());

        return mapper.toDTO(entity);
    }


    @Transactional
    public boolean deleteLetterById(String id) {
        if (!lettersRepository.existsById(id)) return false;
        lettersRepository.deleteById(id);
        log.warn("ACTION=DELETE ID={} - LOSS OF A RECORD", id);
        return true;
    }

    public void updateLetterState(String id, State newState) {

        LettersEntity entity = lettersRepository.findById(id)
                .orElseThrow(() -> new LetterNotFoundException(id));

        entity.setCurrentState(newState);
        entity.incVersion();

        log.info("ACTION=EDIT ID={} VERSION={} CHANGES=STATE", entity.getId(), entity.getVersion()-1);

        lettersRepository.save(entity);

    }


    public void updateContent(String id, String newContent) {
        Optional<LettersEntity> optional = lettersRepository.findById(id);

        if (optional.isEmpty()) {
            throw new LetterNotFoundException(id);
        }

        LettersEntity entity = optional.get();

        versionServices.saveOldVersion(entity);

        entity.setContent(newContent);
        entity.incVersion();

        log.info("ACTION=EDIT ID={} VERSION={} CHANGES=CONTENT", entity.getId(), entity.getVersion()-1);


        lettersRepository.save(entity);

    }


    public LettersDTO getLetterById(String id) {

        Optional<LettersEntity> optional = lettersRepository.findById(id);

        if (optional.isEmpty()) {
            log.error("ACTION=READ FAILED ID={} - Letter not found", id);
            throw new LetterNotFoundException(id);
        }

        LettersEntity entity = optional.get();
        log.info("ACTION=READ ID={}", id);

        return mapper.toDTO(entity);
    }


    private List<LettersDTO> getLettersByName(String name) {
        return lettersRepository.findBySenderOrReceiver(name, name)
                .stream()
                .map(letter -> mapper.toDTO(letter))
                .toList();
    }

    private List<LettersDTO> getLettersByState(String state) {
        return lettersRepository.findByCurrentState(state)
                .stream()
                .map(letter -> mapper.toDTO(letter))
                .toList();

    }

    private List<LettersDTO> getLettersByDate(Integer date) {
        return lettersRepository.findByApproximateYear(date)
                .stream()
                .map(letter -> mapper.toDTO(letter))
                .toList();
    }

    public List<LettersDTO> getByKeyword(String keyword) {
        return lettersRepository.findByContentContainingIgnoreCase(keyword)
                .stream()
                .map(letter -> mapper.toDTO(letter))
                .toList();
    }


    public List<LettersDTO> getLettersFiltered(String name, Integer date, String keyword, String state) {
        Set<LettersDTO> dtos = new HashSet<>();

        if (name != null) dtos.addAll(getLettersByName(name));

        if (date != null) dtos.addAll(getLettersByDate(date));

        if (keyword != null) dtos.addAll(getByKeyword(keyword));

        if (state != null) dtos.addAll(getLettersByState(state));

        return new ArrayList<>(dtos);
    }


    public String fixLetter(String id) {
        Optional<LettersEntity> optional = lettersRepository.findById(id);

        if (optional.isEmpty())
            throw new LetterNotFoundException(id);

        if (optional.get().getCurrentState() != State.DEPRECATED)
            throw new InvalidStateExeption("Letter does not need fixing");

        LettersEntity entity = optional.get();

        String contentToFix = entity.getContent();

        entity.setContent(simulateFixingContent(contentToFix));
        entity.setCurrentState(State.READABLE);
        entity.incVersion();
        lettersRepository.save(entity);

        return entity.getContent();

    }




}
