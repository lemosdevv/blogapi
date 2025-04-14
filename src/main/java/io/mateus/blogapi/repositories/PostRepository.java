package io.mateus.blogapi.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import io.mateus.blogapi.model.Post;
import io.mateus.blogapi.model.Tag;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByCreatedBy(Long userId, Pageable pageable);

    Page<Post> findByCategory(Long categoryId, Pageable pageable);

    Page<Post> findByTags(List<Tag> tags, Pageable pageable);

    Long countByCreatedBy(Long userId);

}
