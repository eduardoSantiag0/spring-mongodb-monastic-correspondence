package com.spring_mongodb_monastic_correspondence.application;

import com.spring_mongodb_monastic_correspondence.domain.dtos.UpdateLetterDTO;
import com.spring_mongodb_monastic_correspondence.domain.model.State;
import com.spring_mongodb_monastic_correspondence.domain.services.LettersService;
import com.spring_mongodb_monastic_correspondence.domain.dtos.LettersDTO;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<?> saveLetters(@Validated @RequestBody LettersDTO dto) {
        return ResponseEntity.ok(lettersService.insertLetter(dto));
    }

    @GetMapping()
    public ResponseEntity<List<LettersDTO>> getLetters(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer date,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String state ) {
        return ResponseEntity.ok(lettersService.getLettersFiltered(name, date, keyword, state));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LettersDTO> getLetterById(@PathVariable String id) {
        return ResponseEntity.ok(lettersService.getLetterById(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLetterById(@PathVariable  String id) {
        if (lettersService.deleteLetterById(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Optional<String>> fixDeprecatedLetter(@PathVariable String id) {
        String fixedLetter = lettersService.fixLetter(id);
        if (fixedLetter.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(fixedLetter.describeConstable());
    }

    @PatchMapping("/update/{id}")
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
