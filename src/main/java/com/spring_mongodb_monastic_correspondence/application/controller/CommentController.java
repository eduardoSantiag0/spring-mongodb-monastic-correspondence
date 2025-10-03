package com.spring_mongodb_monastic_correspondence.application.controller;

import com.spring_mongodb_monastic_correspondence.application.services.CommentService;
import com.spring_mongodb_monastic_correspondence.infra.dtos.CommentResponse;
import com.spring_mongodb_monastic_correspondence.infra.dtos.CreateCommentDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/monastic-correspondence")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Operation(summary = "Add a comment to a letter", description = "Adds a new comment to the letter identified by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment successfully added",
                    content = @Content(schema = @Schema(implementation = CommentResponse.class))),
            @ApiResponse(responseCode = "404", description = "Letter not found")
    })
    @PostMapping("/{id}/comment")
    public ResponseEntity<CommentResponse> addComment(
            @Parameter(description = "ID of the letter to comment on", required = true)
            @PathVariable String id,

            @Parameter(description = "Comment details", required = true)
            @RequestBody CreateCommentDTO dto) {
        var response = commentService.addComment(id, dto);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Get comments of a letter", description = "Returns a paginated list of comments for the letter identified by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of comments retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CommentResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Letter not found")
    })
    @GetMapping("/{id}/comment")
    public ResponseEntity<List<CommentResponse>> seaarchCommentsInLetter( @Parameter(description = "ID of the letter to retrieve comments for", required = true)
                                                                              @PathVariable String id,

                                                                         @RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size) {
        var resultPage = commentService.searchCommentsInLetter(id, page, size);
        List<CommentResponse> comments = resultPage.getContent();
        return ResponseEntity.ok(comments);
    }

}
