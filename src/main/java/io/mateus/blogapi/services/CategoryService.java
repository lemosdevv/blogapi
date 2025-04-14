package io.mateus.blogapi.services;


import org.springframework.http.ResponseEntity;

import io.mateus.blogapi.model.Category;
import io.mateus.blogapi.payload.response.ApiResponse;
import io.mateus.blogapi.payload.response.PagedResponse;
import io.mateus.blogapi.security.UserPrincipal;

public interface CategoryService {

    PagedResponse<Category> getAllCategories(int page, int size);

    ResponseEntity<Category> getCategoryById(Long id);

    ResponseEntity<Category> addCategory(Category category, UserPrincipal currentUser);

    ResponseEntity<Category> updateCategory(Long id, Category newCategory, UserPrincipal currentUser);

    ResponseEntity<ApiResponse> deleteCategory(Long id, UserPrincipal currentUser);
}
