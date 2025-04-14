package io.mateus.blogapi.services.impl;

import static io.mateus.blogapi.utils.AppConstants.CREATED_AT;
import static io.mateus.blogapi.utils.AppConstants.ID;
import static io.mateus.blogapi.utils.AppConstants.TODO;
import static io.mateus.blogapi.utils.AppConstants.YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.mateus.blogapi.exception.BadRequestException;
import io.mateus.blogapi.exception.ResourceNotFoundException;
import io.mateus.blogapi.exception.UnauthorizedException;
import io.mateus.blogapi.model.Todo;
import io.mateus.blogapi.model.user.User;
import io.mateus.blogapi.payload.response.ApiResponse;
import io.mateus.blogapi.payload.response.PagedResponse;
import io.mateus.blogapi.repositories.TodoRepository;
import io.mateus.blogapi.repositories.UserRepository;
import io.mateus.blogapi.security.UserPrincipal;
import io.mateus.blogapi.services.TodoService;
import io.mateus.blogapi.utils.AppConstants;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    // Método para marcar uma tarefa como concluída
    @Override
    public Todo completeTodo(Long id, UserPrincipal currentUser) {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(TODO, ID, id));

        User user = userRepository.getUser(currentUser);

        if (todo.getUser().getId().equals(user.getId())) {
            todo.setCompleted(Boolean.TRUE);
            return todoRepository.save(todo);
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION);
        throw new UnauthorizedException(apiResponse);
    }

    // Método para reverter uma tarefa não concluída
    @Override
    public Todo unCompleteTodo(Long id, UserPrincipal currentUser) {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(TODO, ID, id));

        User user = userRepository.getUser(currentUser);

        if (todo.getUser().getId().equals(user.getId())) {
            todo.setCompleted(Boolean.FALSE);
            return todoRepository.save(todo);
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION);
        throw new UnauthorizedException(apiResponse);
    }

    // Método para obter todas as tarefas de um usuário com paginação
    @Override
    public PagedResponse<Todo> getAllTodos(UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

        Page<Todo> todos = todoRepository.findByCreatedBy(currentUser.getId(), pageable);

        List<Todo> content = todos.getNumberOfElements() == 0 ? List.of() : todos.getContent();

        return new PagedResponse<>(content, todos.getNumber(), todos.getSize(), todos.getTotalElements(),
                todos.getTotalPages(), todos.isLast());
    }

    // Método para adicionar uma nova tarefa
    @Override
    public Todo addTodo(Todo todo, UserPrincipal currentUser) {
        User user = userRepository.getUser(currentUser);

        todo.setUser(user);

        return todoRepository.save(todo);
    }

    // Método para obter uma tarefa específica
    @Override
    public Todo getTodo(Long id, UserPrincipal currentUser) {
        User user = userRepository.getUser(currentUser);

        Todo todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(TODO, ID, "id"));

        if (todo.getUser().getId().equals(user.getId())) {
            return todo;
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION);
        throw new UnauthorizedException(apiResponse);
    }

    // Método para atualizar uma tarefa
    @Override
    public Todo updateTodo(Long id, Todo newTodo, UserPrincipal currentUser) {
        User user = userRepository.getUser(currentUser);
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(TODO, ID, "id"));

        if (todo.getUser().getId().equals(user.getId())) {
            todo.setTitle(newTodo.getTitle());
            todo.setCompleted(newTodo.getCompleted());
            return todoRepository.save(todo);
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION);
        throw new UnauthorizedException(apiResponse);
    }

    // Método para deletar uma tarefa
    @Override
    public ApiResponse deleteTodo(Long id, UserPrincipal currentUser) {
        User user = userRepository.getUser(currentUser);
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(TODO, ID, "id"));

        if (todo.getUser().getId().equals(user.getId())) {

            todoRepository.delete(todo);
            return new ApiResponse(Boolean.TRUE, "Todo deleted successfully!");
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION);
        throw new UnauthorizedException(apiResponse);

    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size < 0) {
            throw new BadRequestException("Size number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }
}
