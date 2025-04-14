package io.mateus.blogapi.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;

import java.util.Arrays;
import io.mateus.blogapi.model.Album;
import io.mateus.blogapi.model.role.RoleName;
import io.mateus.blogapi.model.user.User;
import io.mateus.blogapi.payload.request.AlbumRequest;
import io.mateus.blogapi.payload.response.AlbumResponse;
import io.mateus.blogapi.payload.response.ApiResponse;
import io.mateus.blogapi.payload.response.PagedResponse;
import io.mateus.blogapi.repositories.AlbumRepository;
import io.mateus.blogapi.repositories.UserRepository;
import io.mateus.blogapi.security.UserPrincipal;
import io.mateus.blogapi.services.AlbumService;
import io.mateus.blogapi.utils.AppUtils;
import io.mateus.blogapi.exception.BlogapiException;
import io.mateus.blogapi.exception.ResourceNotFoundException;

import static io.mateus.blogapi.utils.AppConstants.ID;

@Service
public class AlbumServiceImpl implements AlbumService {

    private static final String CREATED_AT = "createdAt";
    private static final String ALBUM_STR = "Album";
    private static final String YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION = "You don't have permission to make this operation";

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PagedResponse<AlbumResponse> getAllAlbuns(int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

        Page<Album> albums = albumRepository.findAll(pageable);

        if (albums.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), albums.getNumber(), albums.getSize(),
                    albums.getTotalElements(),
                    albums.getTotalPages(), albums.isLast());
        }

        List<AlbumResponse> albumResonse = Arrays.asList(modelMapper.map(albums.getContent(), AlbumResponse[].class));

        return new PagedResponse<>(albumResonse, albums.getNumber(), albums.getSize(), albums.getTotalElements(),
                albums.getTotalPages(), albums.isLast());
    }

    @Override
    public ResponseEntity<Album> addAlbum(AlbumRequest albumRequest, UserPrincipal currentUser) {
        User user = userRepository.getUser(currentUser);

        Album album = new Album();

        modelMapper.map(albumRequest, album);

        album.setUser(user);

        Album newAlbum = albumRepository.save(album);

        return new ResponseEntity<>(newAlbum, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Album> getAlbum(Long id) {
        Album album = albumRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ALBUM_STR, ID, id));

        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Album> updateAlbum(Long id, AlbumRequest newAlbum, UserPrincipal currentUser) {
        Album album = albumRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ALBUM_STR, ID, id));

        User user = userRepository.getUser(currentUser);

        if (album.getUser().getId().equals(user.getId()) || currentUser.getAuthorities()
                .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {

            album.setTitle(newAlbum.getTitle());

            Album updateAlbum = albumRepository.save(album);

            AlbumResponse albumResponse = new AlbumResponse();
            modelMapper.map(updateAlbum, albumResponse);

            return new ResponseEntity<>(updateAlbum, HttpStatus.OK);
        }

        throw new BlogapiException(HttpStatus.UNAUTHORIZED, YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteAlbum(Long id, UserPrincipal currentUser) {
        Album album = albumRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ALBUM_STR, ID, id));

        User user = userRepository.getUser(currentUser);

        if (album.getUser().getId().equals(user.getId()) || currentUser.getAuthorities()
                .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {

            albumRepository.deleteById(id);

            return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "You successfully deleted album"), HttpStatus.OK);
        }

        throw new BlogapiException(HttpStatus.UNAUTHORIZED, YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION);
    }

    @Override
    public PagedResponse<Album> getUserAlbums(String username, int page, int size) {
        User user = userRepository.findByUsername(username)  // Usando findByUsername se esse método existir
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

        Page<Album> albums = albumRepository.findByCreatedBy(user.getId(), pageable);

        List<Album> content = albums.getNumberOfElements() > 0 ? albums.getContent() : Collections.emptyList();

        return new PagedResponse<>(content, albums.getNumber(), albums.getSize(), albums.getTotalElements(),
                albums.getTotalPages(), albums.isLast());
    }


}
