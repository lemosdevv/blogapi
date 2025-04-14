package io.mateus.blogapi.services;


import io.mateus.blogapi.model.Comment;
import io.mateus.blogapi.payload.request.CommentRequest;
import io.mateus.blogapi.payload.response.ApiResponse;
import io.mateus.blogapi.payload.response.PagedResponse;
import io.mateus.blogapi.security.UserPrincipal;

public interface CommentService {

    PagedResponse<Comment> getAllComments(Long postId, int page, int size);

	Comment addComment(CommentRequest commentRequest, Long postId, UserPrincipal currentUser);

	Comment getComment(Long postId, Long id);

	Comment updateComment(Long postId, Long id, CommentRequest commentRequest, UserPrincipal currentUser);

	ApiResponse deleteComment(Long postId, Long id, UserPrincipal currentUser);
}
