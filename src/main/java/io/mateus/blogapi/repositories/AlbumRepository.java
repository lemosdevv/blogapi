package io.mateus.blogapi.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import io.mateus.blogapi.model.Album;

public interface AlbumRepository extends JpaRepository<Album, Long>{
    Page<Album> findByCreatedBy(Long userId, Pageable pageable);
}
