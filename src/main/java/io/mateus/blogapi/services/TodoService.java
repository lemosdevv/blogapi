package io.mateus.blogapi.services;

import io.mateus.blogapi.model.Todo;
import io.mateus.blogapi.payload.response.ApiResponse;
import io.mateus.blogapi.payload.response.PagedResponse;
import io.mateus.blogapi.security.UserPrincipal;

public interface TodoService {

    Todo completeTodo(Long id, UserPrincipal currentUser);

    Todo unCompleteTodo(Long id, UserPrincipal currentUser);

    PagedResponse<Todo> getAllTodos(UserPrincipal currentUser, int page, int size);

    Todo addTodo(Todo todo, UserPrincipal currentUser);

    Todo getTodo(Long id, UserPrincipal currentUser);

    Todo updateTodo(Long id, Todo newTodo, UserPrincipal currentUser);

    ApiResponse deleteTodo(Long id, UserPrincipal currentUser);

}
