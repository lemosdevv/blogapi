package io.mateus.blogapi.controllers;

import io.mateus.blogapi.exception.UnauthorizedException;
import io.mateus.blogapi.model.Category;
import io.mateus.blogapi.payload.response.ApiResponse;
import io.mateus.blogapi.payload.response.PagedResponse;
import io.mateus.blogapi.security.CurrentUser;
import io.mateus.blogapi.security.UserPrincipal;
import io.mateus.blogapi.services.CategoryService;
import io.mateus.blogapi.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public PagedResponse<Category> getAllCategories(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size){
        return categoryService.getAllCategories(page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable(name = "id") Long id){
        return categoryService.getCategoryById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Category> addCategory(@Valid @RequestBody Category category,
                                                @CurrentUser UserPrincipal currentUser){

        return categoryService.addCategory(category, currentUser);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Category> updateCategory(@PathVariable(name = "id") Long id,
                                                   @Valid @RequestBody Category category, @CurrentUser UserPrincipal currentUser) throws UnauthorizedException {
        return categoryService.updateCategory(id, category, currentUser);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable(name = "id") Long id,
                                                      @CurrentUser UserPrincipal currentUser) throws UnauthorizedException {
        return categoryService.deleteCategory(id, currentUser);
    }

}
