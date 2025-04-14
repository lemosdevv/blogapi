package io.mateus.blogapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import io.mateus.blogapi.model.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String name);
}
