package io.mateus.blogapi.services.impl;

import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import io.mateus.blogapi.exception.BadRequestException;
import io.mateus.blogapi.exception.ResourceNotFoundException;
import io.mateus.blogapi.exception.UnauthorizedException;
import io.mateus.blogapi.model.Category;
import io.mateus.blogapi.model.Post;
import io.mateus.blogapi.model.Tag;
import io.mateus.blogapi.model.role.RoleName;
import io.mateus.blogapi.model.user.User;
import io.mateus.blogapi.payload.request.PostRequest;
import io.mateus.blogapi.payload.response.ApiResponse;
import io.mateus.blogapi.payload.response.PagedResponse;
import io.mateus.blogapi.payload.response.PostResponse;
import io.mateus.blogapi.repositories.CategoryRepository;
import io.mateus.blogapi.repositories.PostRepository;
import io.mateus.blogapi.repositories.TagRepository;
import io.mateus.blogapi.repositories.UserRepository;
import io.mateus.blogapi.security.UserPrincipal;
import io.mateus.blogapi.services.PostService;
import io.mateus.blogapi.utils.AppConstants;
import io.mateus.blogapi.utils.AppUtils;

import static io.mateus.blogapi.utils.AppConstants.CREATED_AT;
import static io.mateus.blogapi.utils.AppConstants.ID;
import static io.mateus.blogapi.utils.AppConstants.CATEGORY;
import static io.mateus.blogapi.utils.AppConstants.POST;
import static io.mateus.blogapi.utils.AppConstants.TAG;
import static io.mateus.blogapi.utils.AppConstants.USER;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TagRepository tagRepository;

    // Método para listar todos os posts
    @Override
    public PagedResponse<Post> getAllPosts(int page, int size) {
        validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstants.CREATED_AT);

        Page<Post> posts = postRepository.findAll(pageable);

        List<Post> content = posts.getNumberOfElements() == 0 ? List.of()
                : posts.getContent();

        return new PagedResponse<>(content, posts.getNumber(), posts.getSize(), posts.getTotalElements(),
                posts.getTotalPages(), posts.isLast());

    }

    // Método para listar todos os posts de um usuário específico
    @Override
    public PagedResponse<Post> getAllPostsByUsername(String username, int page, int size) {
        validatePageNumberAndSize(page, size);

        User user = userRepository.getUserByName(username);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

        Page<Post> posts = postRepository.findByCreatedBy(user.getId(), pageable);

        List<Post> content = posts.getNumberOfElements() == 0 ? Collections.emptyList()
                : posts.getContent();

        return new PagedResponse<>(content, posts.getNumber(), posts.getSize(), posts.getTotalElements(),
                posts.getTotalPages(), posts.isLast());

    }

    // Método para listar todos os posts de uma categoria específica
    @Override
    public PagedResponse<Post> getAllPostsByCategory(Long id, int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY, ID, id));

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

        Page<Post> posts = postRepository.findByCategory(category.getId(), pageable);

        List<Post> content = posts.getNumberOfElements() == 0 ? Collections.emptyList()
                : posts.getContent();

        return new PagedResponse<>(content, posts.getNumber(), posts.getSize(), posts.getTotalElements(),
                posts.getTotalPages(), posts.isLast());
    }

    // Método para buscar posts por tag específica com paginação.
    @Override
    public PagedResponse<Post> getAllPostsByTag(Long id, int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(TAG, ID, id));

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

        Page<Post> posts = postRepository.findByTags(Collections.singletonList(tag), pageable);

        List<Post> content = posts.getNumberOfElements() == 0 ? Collections.emptyList()
                : posts.getContent();

        return new PagedResponse<>(content, posts.getNumber(), posts.getSize(), posts.getTotalElements(),
                posts.getTotalPages(), posts.isLast());
    }

    // Método para atualizar um post existente
    @Override
    public Post updatePost(Long id, PostRequest newPostRequest, UserPrincipal currentUser) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(POST, ID, id));

        Category category = categoryRepository.findById(newPostRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY, ID, newPostRequest.getCategoryId()));
    
        if (post.getCreatedBy().equals(currentUser.getId()) || currentUser.getAuthorities()
                .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            post.setTitle(newPostRequest.getTitle());
            post.setBody(newPostRequest.getBody());
            post.setCategory(category);

            return postRepository.save(post);
        } 
           
        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to update this post.");
        throw new UnauthorizedException(apiResponse); 
    }

    // Método para deletar um post
    @Override
    public ApiResponse deletePost(Long id, UserPrincipal currentUser) {
		Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(POST, ID, id));

		if (post.getUser().getId().equals(currentUser.getId())
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			postRepository.deleteById(id);
			return new ApiResponse(Boolean.TRUE, "You successfully deleted post");
		}

		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to delete this post");
		throw new UnauthorizedException(apiResponse);
    }
    
    // Método para adicionar um novo post.
    @Override
    public PostResponse addPost(PostRequest postRequest, UserPrincipal currentUser) {
        User user = userRepository.findById(currentUser.getId())
				.orElseThrow(() -> new ResourceNotFoundException(USER, ID, 1L));

		Category category = categoryRepository.findById(postRequest.getCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException(CATEGORY, ID, postRequest.getCategoryId()));

		List<Tag> tags = new ArrayList<>(postRequest.getTags().size());
		for (String name : postRequest.getTags()) {
			Tag tag = tagRepository.findByName(name);
			tag = tag == null ? tagRepository.save(new Tag(name)) : tag;
			tags.add(tag);
		}

		Post post = new Post();
		post.setBody(postRequest.getBody());
		post.setTitle(postRequest.getTitle());
		post.setCategory(category);
		post.setUser(user);
		post.setTags(tags);

		Post newPost = postRepository.save(post);
		PostResponse postResponse = new PostResponse();

		postResponse.setTitle(newPost.getTitle());
		postResponse.setBody(newPost.getBody());
		postResponse.setCategory(newPost.getCategory().getName());

		List<String> tagNames = new ArrayList<>(newPost.getTags().size());
		for (Tag tag : newPost.getTags()) {
			tagNames.add(tag.getName());
		}

		postResponse.setTags(tagNames);

		return postResponse;
    }

    // Método para buscar um post pelo ID.
    @Override
    public Post getPostId(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(POST, ID, id));
    }

    // Método para validar o número da página e o tamanho
    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }
        if (size < 0) {
            throw new BadRequestException("Size number cannot be less than one.");
        }
        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Size number cannot be greater than " + AppConstants.MAX_PAGE_SIZE + ".");
        }
    }
}
