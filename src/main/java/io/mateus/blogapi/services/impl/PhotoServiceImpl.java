package io.mateus.blogapi.services.impl;

import static io.mateus.blogapi.utils.AppConstants.CREATED_AT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import io.mateus.blogapi.exception.ResourceNotFoundException;
import io.mateus.blogapi.exception.UnauthorizedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import io.mateus.blogapi.model.Album;
import io.mateus.blogapi.model.Photo;
import io.mateus.blogapi.model.role.RoleName;
import io.mateus.blogapi.payload.request.PhotoRequest;
import io.mateus.blogapi.payload.response.ApiResponse;
import io.mateus.blogapi.payload.response.PagedResponse;
import io.mateus.blogapi.payload.response.PhotoResponse;
import io.mateus.blogapi.repositories.AlbumRepository;
import io.mateus.blogapi.repositories.PhotoRepository;
import io.mateus.blogapi.security.UserPrincipal;
import io.mateus.blogapi.services.PhotoService;
import io.mateus.blogapi.utils.AppUtils;

import static io.mateus.blogapi.utils.AppConstants.ID;
import static io.mateus.blogapi.utils.AppConstants.PHOTO;
import static io.mateus.blogapi.utils.AppConstants.ALBUM;

@Service
public class PhotoServiceImpl implements PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private AlbumRepository albumRepository;

    // Método que retorna uma lista paginada de fotos.
    @Override
    public PagedResponse<PhotoResponse> getAllPhotos(int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

        Page<Photo> photos = photoRepository.findAll(pageable);

        List<PhotoResponse> photoResponses = new ArrayList<>(photos.getContent().size());
        for (Photo photo : photos.getContent()) {
            photoResponses.add(new PhotoResponse(photo.getId(), photo.getTitle(), photo.getUrl(),
                    photo.getThumbnailUrl(), photo.getAlbum().getId()));
        }

        if (photos.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), photos.getNumber(), photos.getSize(),
                    photos.getTotalElements(), photos.getTotalPages(), photos.isLast());
        }

        return new PagedResponse<>(photoResponses, photos.getNumber(), photos.getSize(), photos.getTotalElements(),
                photos.getTotalPages(), photos.isLast());
    }

    // Método para buscar uma foto específica pelo ID.
    @Override
    public PhotoResponse getPhoto(Long id) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PHOTO, ID, id));

        return new PhotoResponse(photo.getId(), photo.getTitle(), photo.getUrl(), photo.getThumbnailUrl(),
                photo.getAlbum().getId());
    }

    // Método para atualizar uma foto.
    @Override
    public PhotoResponse updatePhoto(Long id, PhotoRequest photoRequest, UserPrincipal currentUser) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ALBUM, ID, photoRequest.getAlbumId()));

        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PHOTO, ID, id));

        if (photo.getAlbum().getUser().getId().equals(currentUser.getId())
                || currentUser.getAuthorities()
                        .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {

            photo.setTitle(photoRequest.getTitle());
            photo.setThumbnailUrl(photoRequest.getThumbnailUrl());
            photo.setAlbum(album);

            Photo updatePhoto = photoRepository.save(photo);
            return new PhotoResponse(updatePhoto.getId(), updatePhoto.getTitle(),
                    updatePhoto.getUrl(), updatePhoto.getThumbnailUrl(), updatePhoto.getAlbum().getId());
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to update this photo");
        throw new UnauthorizedException(apiResponse);
    }

    // Método para adicionar uma nova foto a um álbum.
    @Override
    public PhotoResponse addPhoto(PhotoRequest photoRequest, UserPrincipal currentUser) {
        Album album = albumRepository.findById(photoRequest.getAlbumId())
                .orElseThrow(() -> new ResourceNotFoundException(ALBUM, ID, photoRequest.getAlbumId()));

        if (album.getUser().getId().equals(currentUser.getId())) {

            Photo photo = new Photo(photoRequest.getTitle(), photoRequest.getUrl(), photoRequest.getThumbnailUrl(),
                    album);

            Photo newPhoto = photoRepository.save(photo);
            return new PhotoResponse(newPhoto.getId(), newPhoto.getTitle(), newPhoto.getUrl(),
                    newPhoto.getThumbnailUrl(), newPhoto.getAlbum().getId());
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to add this photo");
        throw new UnauthorizedException(apiResponse);
    }

    // Método para deletar uma foto.
    @Override
    public ApiResponse deletePhoto(Long id, UserPrincipal currentUser) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PHOTO, ID, id));

        if (photo.getAlbum().getUser().getId().equals(currentUser.getId())
                || currentUser.getAuthorities()
                        .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {

            photoRepository.deleteById(id);
            return new ApiResponse(Boolean.TRUE, "You successfully deleted photo");
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to delete this photo");
        throw new UnauthorizedException(apiResponse);
    }

    // Método para buscar todas as fotos de um álbum específico.
    @Override
    public PagedResponse<PhotoResponse> getAllPhotosByAlbum(Long albumId, int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

        Page<Photo> photos = photoRepository.findByAlbumId(albumId, pageable);

        List<PhotoResponse> photoResponses = new ArrayList<>(photos.getContent().size());
        for (Photo photo : photos.getContent()) {
            photoResponses.add(new PhotoResponse(photo.getId(), photo.getTitle(), photo.getUrl(),
                    photo.getThumbnailUrl(), photo.getAlbum().getId()));
        }

        if (photos.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), photos.getNumber(), photos.getSize(),
                    photos.getTotalElements(), photos.getTotalPages(), photos.isLast());
        }

        return new PagedResponse<>(photoResponses, photos.getNumber(), photos.getSize(), photos.getTotalElements(),
                photos.getTotalPages(), photos.isLast());
    }

}
