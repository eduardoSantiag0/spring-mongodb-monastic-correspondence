package com.spring_mongodb_monastic_correspondence.application;

import com.spring_mongodb_monastic_correspondence.domain.dtos.CreateLetterDTO;
import com.spring_mongodb_monastic_correspondence.domain.dtos.UpdateLetterDTO;
import com.spring_mongodb_monastic_correspondence.domain.model.State;
import com.spring_mongodb_monastic_correspondence.domain.services.LettersService;
import com.spring_mongodb_monastic_correspondence.domain.dtos.LettersDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/monastic-correspondence")
public class LettersController {

    @Autowired
    private LettersService lettersService;

    @PostMapping()
    @Operation(summary = "Create a letter", description = "Creates a new monastic letter.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Letter created"),
            @ApiResponse(responseCode = "400", description = "Incomplete or invalid data")
    })
    public ResponseEntity<?> saveLetters(@Valid @RequestBody CreateLetterDTO dto) {
        LettersDTO created = lettersService.insertLetter(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping()
    @Operation(summary = "Filter letters", description = "Returns a list of letters based on optional filters.")
    @ApiResponse(responseCode = "200", description = "List of letters returned")
    public ResponseEntity<List<LettersDTO>> getLetters(
            @Parameter(description = "Name of sender or receiver (partial or full match)", example = "Father James")
            @RequestParam(required = false) String name,

            @Parameter(description = "Approximate year of the letter", example = "1860")
            @RequestParam(required = false) Integer date,

            @Parameter(description = "Keyword contained in the letter content", example = "Wi-Fi")
            @RequestParam(required = false) String keyword,

            @Parameter(description = "Current state of the letter", example = "READABLE")
            @RequestParam(required = false) String state
    ) {
        return ResponseEntity.ok(lettersService.getLettersFiltered(name, date, keyword, state));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a letter by ID", description = "Returns a specific letter by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Letter found"),
            @ApiResponse(responseCode = "404", description = "Letter not found")
    })
    public ResponseEntity<LettersDTO> getLetterById(@PathVariable String id) {
        return ResponseEntity.ok(lettersService.getLetterById(id));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a letter", description = "Deletes a letter by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Letter deleted"),
            @ApiResponse(responseCode = "404", description = "Letter not found")
    })
    public ResponseEntity<Void> deleteLetterById(@PathVariable  String id) {
        if (lettersService.deleteLetterById(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }


    @PatchMapping("/{id}")
    @Operation(summary = "Fix a deprecated letter", description = "Fixes the deprecated content of a letter.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Letter fixed"),
            @ApiResponse(responseCode = "404", description = "Letter not found or not deprecated")
    })
    public ResponseEntity<Optional<String>> fixDeprecatedLetter(@PathVariable String id) {
        String fixedLetter = lettersService.fixLetter(id);
        if (fixedLetter.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(fixedLetter.describeConstable());
    }

    @PatchMapping("/update/{id}")
    @Operation(summary = "Update letter state and/or content", description = "Updates the state and/or content of a letter. The user must provide at least one of the fields: `new_state` or `new_content`.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Letter updated successfully"),
            @ApiResponse(responseCode = "404", description = "Letter not found")
    })
    public ResponseEntity<Void> updateLetterState (@PathVariable String id, @RequestBody UpdateLetterDTO dto) {

        if (dto.new_state() != null) {
            State newStateEnum = State.valueOf(dto.new_state());
            lettersService.updateLetterState(id, newStateEnum);
        }

        if (dto.new_content() != null) {
            lettersService.updateContent(id, dto.new_content());
        }

        return ResponseEntity.noContent().build();
    }

}
