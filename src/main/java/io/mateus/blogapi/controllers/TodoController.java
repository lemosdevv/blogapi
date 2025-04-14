package io.mateus.blogapi.controllers;

import io.mateus.blogapi.model.Todo;
import io.mateus.blogapi.model.user.User;
import io.mateus.blogapi.payload.response.ApiResponse;
import io.mateus.blogapi.payload.response.PagedResponse;
import io.mateus.blogapi.security.CurrentUser;
import io.mateus.blogapi.security.UserPrincipal;
import io.mateus.blogapi.services.TodoService;
import io.mateus.blogapi.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PagedResponse<Todo>> getAllTodos(
        @CurrentUser UserPrincipal currentUser,
        @RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
        @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

            PagedResponse<Todo> response = todoService.getAllTodos(currentUser, page, size);

            return new ResponseEntity< >(response, HttpStatus.OK);
        }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Todo> getTodo(@PathVariable(value = "id")Long id, @CurrentUser UserPrincipal currentUser){
        Todo todo = todoService.getTodo(id, currentUser);

        return new ResponseEntity<>(todo, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Todo> addTodo(@Valid @RequestBody Todo todo, @CurrentUser UserPrincipal currentUser){
        Todo newTodo = todoService.addTodo(todo, currentUser);

        return new ResponseEntity<>(newTodo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Todo> updateTodo(@PathVariable(value = "id")Long id,
                                           @Valid @RequestBody Todo newTodo,
                                           @CurrentUser UserPrincipal currentUser){
        Todo updateTodo = todoService.updateTodo(id, newTodo, currentUser);

        return new ResponseEntity<>(updateTodo, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> deleteTodo(@PathVariable(value = "id")Long id, @CurrentUser UserPrincipal currentUser){
        ApiResponse deleteTodo = todoService.deleteTodo(id, currentUser);

        return new ResponseEntity<>(deleteTodo, HttpStatus.OK);
    }

    @PutMapping("/{id}/complete")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Todo> completeTodo(@PathVariable(value = "id")Long id, @CurrentUser UserPrincipal currentUser){
        Todo todo = todoService.completeTodo(id, currentUser);

        return new ResponseEntity<>(todo, HttpStatus.OK);
    }

    @PutMapping("/{id}/unComplete")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Todo> unCompleteTodo(@PathVariable(value = "id") Long id, @CurrentUser UserPrincipal currentUser) {

        Todo todo = todoService.unCompleteTodo(id, currentUser);

        return new ResponseEntity< >(todo, HttpStatus.OK);
    }
}
