package com.spring_mongodb_monastic_correspondence.infra;

import com.spring_mongodb_monastic_correspondence.infra.dtos.LettersDTO;
import com.spring_mongodb_monastic_correspondence.infra.entities.LettersEntity;
import org.springframework.stereotype.Component;

@Component
public class LettersMapper {

    public LettersEntity toEntity (LettersDTO dto) {
        return new LettersEntity(dto.id(), dto.sender(), dto.receiver(), dto.content(), dto.approximateYear(), dto.currentState(), dto.version());
    }

    public LettersDTO toDTO (LettersEntity entity) {
        return new LettersDTO(entity.getId(), entity.getSender(), entity.getReceiver(), entity.getContent(), entity.getApproximateYear(), entity.getCurrentState(), entity.getVersion());
    }

}
