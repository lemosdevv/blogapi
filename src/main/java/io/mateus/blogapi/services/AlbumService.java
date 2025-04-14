package io.mateus.blogapi.services;

import org.springframework.http.ResponseEntity;

import io.mateus.blogapi.model.Album;
import io.mateus.blogapi.payload.request.AlbumRequest;
import io.mateus.blogapi.payload.response.AlbumResponse;
import io.mateus.blogapi.payload.response.ApiResponse;
import io.mateus.blogapi.payload.response.PagedResponse;
import io.mateus.blogapi.security.UserPrincipal;

public interface AlbumService {

    PagedResponse<AlbumResponse> getAllAlbuns(int page, int size);

    ResponseEntity<Album> addAlbum(AlbumRequest AlbumRequest, UserPrincipal currentUser);

    ResponseEntity<Album> getAlbum(Long id);

    ResponseEntity<Album> updateAlbum(Long id, AlbumRequest albumRequest, UserPrincipal currentUser);

    ResponseEntity<ApiResponse> deleteAlbum(Long id, UserPrincipal currentUser);

    // Retorna uma lista paginada de álbuns de um usuário específico
    PagedResponse<Album> getUserAlbums(String username, int page, int size);

}
