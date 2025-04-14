package io.mateus.blogapi.controllers;

import io.mateus.blogapi.security.UserPrincipal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.mateus.blogapi.model.Album;
import io.mateus.blogapi.model.Post;
import io.mateus.blogapi.model.user.User;
import io.mateus.blogapi.payload.UserIdentityAvailability;
import io.mateus.blogapi.payload.UserProfile;
import io.mateus.blogapi.payload.UserSummary;
import io.mateus.blogapi.payload.request.InfoRequest;
import io.mateus.blogapi.payload.response.ApiResponse;
import io.mateus.blogapi.payload.response.PagedResponse;
import io.mateus.blogapi.security.CurrentUser;
import io.mateus.blogapi.services.AlbumService;
import io.mateus.blogapi.services.PostService;
import io.mateus.blogapi.services.UserService;
import io.mateus.blogapi.utils.AppConstants;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private AlbumService albumService;

    // Endpoint que retorna um resumo do usuário autenticado.
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserSummary> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = userService.getCurrentUser(currentUser);

        return new ResponseEntity<>(userSummary, HttpStatus.OK);
    }

    // Endpoint que retorna se o usuario está disponível.
    @GetMapping("/checkUsernameAvailability")
    public ResponseEntity<UserIdentityAvailability> checkUsernameAvailability(
            @RequestParam(value = "username") String username) {
        UserIdentityAvailability userIdentityAvailability = userService.checkUsernameAvailability(username);
        return new ResponseEntity<>(userIdentityAvailability, HttpStatus.OK);
    }

    // Endpoint que retorna se o email está disponível.
    @GetMapping("/checkEmailAvailability")
    public ResponseEntity<UserIdentityAvailability> checkEmailAvailability(
            @RequestParam(value = "email") String email) {
        UserIdentityAvailability userIdentityAvailability = userService.checkEmailAvailability(email);
        return new ResponseEntity<>(userIdentityAvailability, HttpStatus.OK);
    }

    // Endpoint que retorna o perfil do usuário, com base no nome de usuário.
    @GetMapping("/{username}/profile")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable(value = "username") String username) {
        UserProfile userProfile = userService.getUserProfile(username);
        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }

    // Endpoint que retorna o número de posts do usuário, com base no nome de
    // usuário.
    @GetMapping("/{username}/posts")
    public ResponseEntity<PagedResponse<Post>> getAllPostsByUsername(@PathVariable(name = "username") String username,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
        PagedResponse<Post> response = postService.getAllPostsByUsername(username, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Endpoint que retorna o número de álbuns do usuário, com base no nome de
    // usuário.
    @GetMapping("/{username}/albums")
    public ResponseEntity<PagedResponse<Album>> getUserAlbums(@PathVariable(name = "username") String username,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
        PagedResponse<Album> response = albumService.getUserAlbums(username, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Endpoint para criar um novo usuário
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User newUser = userService.addUser(user);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    // Endpoint para atualizar um usuário existente
    @PutMapping("/{username}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<User> updateUser(@Valid @RequestBody User newUser,
            @PathVariable(value = "username") String username, @CurrentUser UserPrincipal currentUser) {
        User updatedUser = userService.updateUser(newUser, username, currentUser);

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    // Endpoint para deletar um usuário existente
    @DeleteMapping("/{username}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable(value = "username") String username,
            @CurrentUser UserPrincipal currentUser) {
        ApiResponse apiResponse = userService.deleteUser(username, currentUser);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{username}/giveAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> giveAdmin(@PathVariable(name = "username") String username) {
        ApiResponse apiResponse = userService.giveAdmin(username);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{username}/takeAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> takeAdmin(@PathVariable(name = "username") String username) {
        ApiResponse apiResponse = userService.removeAdmin(username);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/setOrUpdateInfo")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserProfile> setAddress(@CurrentUser UserPrincipal currentUser,
            @Valid @RequestBody InfoRequest infoRequest) {
        UserProfile userProfile = userService.setOrUpdateInfo(currentUser, infoRequest);

        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }
}
