package io.mateus.blogapi.controllers;

import io.mateus.blogapi.payload.request.PhotoRequest;
import io.mateus.blogapi.payload.response.ApiResponse;
import io.mateus.blogapi.payload.response.PagedResponse;
import io.mateus.blogapi.payload.response.PhotoResponse;
import io.mateus.blogapi.security.CurrentUser;
import io.mateus.blogapi.security.UserPrincipal;
import io.mateus.blogapi.services.PhotoService;
import io.mateus.blogapi.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @GetMapping
    public PagedResponse<PhotoResponse> getAllPhotos(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size){

        return photoService.getAllPhotos(page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhotoResponse> getPhoto(@PathVariable(name = "id")Long id){

        PhotoResponse photoResponse = photoService.getPhoto(id);

        return new ResponseEntity<>(photoResponse, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PhotoResponse> addPhoto(@Valid @RequestBody PhotoRequest photoRequest,
                                                  @CurrentUser UserPrincipal currentUser){

        PhotoResponse photoResponse = photoService.addPhoto(photoRequest, currentUser);

        return new ResponseEntity<>(photoResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<PhotoResponse> updatePhoto(@PathVariable(name = "id") Long id,
                                                     @Valid @RequestBody PhotoRequest photoRequest,
                                                     @CurrentUser UserPrincipal currentUser){

        PhotoResponse updatedPhoto = photoService.updatePhoto(id, photoRequest, currentUser);

        return new ResponseEntity<>(updatedPhoto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deletePhoto(@PathVariable(name = "id") Long id,
                                                   @CurrentUser UserPrincipal currentUser){

        ApiResponse deletedPhoto = photoService.deletePhoto(id, currentUser);

        return new ResponseEntity<>(deletedPhoto, HttpStatus.OK);
    }
}
