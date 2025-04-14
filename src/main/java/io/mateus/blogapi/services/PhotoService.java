package io.mateus.blogapi.services;


import io.mateus.blogapi.payload.request.PhotoRequest;
import io.mateus.blogapi.payload.response.ApiResponse;
import io.mateus.blogapi.payload.response.PagedResponse;
import io.mateus.blogapi.payload.response.PhotoResponse;
import io.mateus.blogapi.security.UserPrincipal;

public interface PhotoService {

    PagedResponse<PhotoResponse> getAllPhotos(int page, int size);

    PhotoResponse getPhoto(Long id);

    PhotoResponse updatePhoto(Long id, PhotoRequest photoRequest, UserPrincipal currentUser);

    PhotoResponse addPhoto(PhotoRequest photoRequest, UserPrincipal currentUser);

    ApiResponse deletePhoto(Long id, UserPrincipal currentUser);

    PagedResponse<PhotoResponse> getAllPhotosByAlbum(Long albumId, int page, int size);
}
