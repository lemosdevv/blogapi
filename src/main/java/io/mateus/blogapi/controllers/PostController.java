package io.mateus.blogapi.controllers;

import io.mateus.blogapi.model.Post;
import io.mateus.blogapi.payload.request.PostRequest;
import io.mateus.blogapi.payload.response.ApiResponse;
import io.mateus.blogapi.payload.response.PagedResponse;
import io.mateus.blogapi.payload.response.PostResponse;
import io.mateus.blogapi.security.CurrentUser;
import io.mateus.blogapi.security.UserPrincipal;
import io.mateus.blogapi.services.PostService;
import io.mateus.blogapi.utils.AppConstants;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/post")
public class PostController {

    @Autowired
    private PostService postService;

    // Endpoint GET para retornar todos os posts com paginação
    @GetMapping
    public ResponseEntity<PagedResponse<Post>> getAllPosts(
            @RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(value = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size
    ){
        PagedResponse<Post> response = postService.getAllPosts(page, size);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Endpoint GET para retornar posts por categoria
    @GetMapping("/category/{id}")
    public ResponseEntity<PagedResponse<Post>> getPostsByCategory(
            @RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(value = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @PathVariable(name = "id") Long id){

        PagedResponse<Post> response = postService.getAllPostsByCategory(id, page, size);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Endpoint GET para retornar posts por tag
    @GetMapping("/tag/{id}")
    public ResponseEntity<PagedResponse<Post>> getPostsByTag(
            @RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(value = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @PathVariable(name = "id") Long id){

        PagedResponse<Post> response = postService.getAllPostsByTag(id, page, size);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Endpoint GET para buscar um post por ID
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable(name = "id") Long id){

        Post post = postService.getPostId(id);

        return  new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')") // Somente usuários com papel USER podem criar posts
    public ResponseEntity<PostResponse> addPost(@Valid @RequestBody PostRequest postRequest,
                                                @CurrentUser UserPrincipal currentUser) {

        // Chama o serviço para adicionar um novo post
        PostResponse postResponse = postService.addPost(postRequest, currentUser);

        // Retorna o post criado com status 201 (Created)
        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }

    // Endpoint PUT para atualizar um post, permitido para usuários ou admins
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Post> updatePost(@PathVariable(name = "id") Long id,
                                           @Valid @RequestBody PostRequest newPostRequest,
                                           @CurrentUser UserPrincipal currentUser) {

        // Atualiza o post com os novos dados
        Post post = postService.updatePost(id, newPostRequest, currentUser);

        // Retorna o post atualizado
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    // Endpoint DELETE para remover um post, permitido para usuários ou admins
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable(name = "id") Long id,
                                                  @CurrentUser UserPrincipal currentUser) {

        // Chama o serviço para deletar o post
        ApiResponse apiResponse = postService.deletePost(id, currentUser);

        // Retorna a resposta da operação
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
