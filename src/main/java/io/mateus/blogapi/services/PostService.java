package io.mateus.blogapi.services;

import io.mateus.blogapi.model.Post;
import io.mateus.blogapi.payload.request.PostRequest;
import io.mateus.blogapi.payload.response.ApiResponse;
import io.mateus.blogapi.payload.response.PagedResponse;
import io.mateus.blogapi.payload.response.PostResponse;
import io.mateus.blogapi.security.UserPrincipal;

public interface PostService {

    PagedResponse<Post> getAllPosts(int page, int size);

    PagedResponse<Post> getAllPostsByUsername(String username, int page, int size);

    PagedResponse<Post> getAllPostsByCategory(Long id, int page, int size);

    PagedResponse<Post> getAllPostsByTag(Long id, int page, int size);

    Post updatePost(Long id, PostRequest newPostRequest, UserPrincipal currentUser);

    ApiResponse deletePost(Long id, UserPrincipal currentUser);

    PostResponse addPost(PostRequest postRequest, UserPrincipal currentUser);

    Post getPostId(Long id);
}
