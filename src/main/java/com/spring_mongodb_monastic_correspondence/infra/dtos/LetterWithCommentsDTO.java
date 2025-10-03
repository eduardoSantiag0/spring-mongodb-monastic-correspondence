package com.spring_mongodb_monastic_correspondence.infra.dtos;

import com.spring_mongodb_monastic_correspondence.domain.model.State;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record LetterWithCommentsDTO(

        @Schema(example = "Father James")
        String sender,

        @Schema(example = "Father Kornelius")
        String receiver,

        @Schema(example = "I discovered a sealed chamber with strange plastic-bound texts referencing a deity called Wi-Fi...")
        String content,

        @Schema(example = "1860")
        Integer approximateYear,

        @Schema(example = "READABLE")
        State currentState,

        int version,

        List<CommentResponse> comments

) {

}
