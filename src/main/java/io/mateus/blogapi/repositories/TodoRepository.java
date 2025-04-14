package io.mateus.blogapi.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import io.mateus.blogapi.model.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    Page<Todo> findByCreatedBy(Long userId, Pageable pageable);
}
