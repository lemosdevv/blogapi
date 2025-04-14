package io.mateus.blogapi.controllers;

import io.mateus.blogapi.model.Tag;
import io.mateus.blogapi.payload.response.ApiResponse;
import io.mateus.blogapi.payload.response.PagedResponse;
import io.mateus.blogapi.security.CurrentUser;
import io.mateus.blogapi.security.UserPrincipal;
import io.mateus.blogapi.services.TagService;
import io.mateus.blogapi.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping
    public ResponseEntity<PagedResponse<Tag>> getAllTags(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size){

        PagedResponse<Tag> response = tagService.getAllTags(page, size);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable(name = "id")Long id){
        Tag tag = tagService.getTag(id);

        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity addTag(@Valid @RequestBody Tag tag, @CurrentUser UserPrincipal currentUser){
        Tag newTag = tagService.addTag(tag, currentUser);

        return new ResponseEntity<>(newTag, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Tag> updateTag(@PathVariable(name = "id") Long id, @Valid @RequestBody Tag tag, @CurrentUser UserPrincipal currentUser) {

        Tag updatedTag = tagService.updateTag(id, tag, currentUser);

        return new ResponseEntity<>(updatedTag, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteTag(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal currentUser){
        ApiResponse apiResponse = tagService.deleteTag(id, currentUser);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
