package io.mateus.blogapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import io.mateus.blogapi.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
