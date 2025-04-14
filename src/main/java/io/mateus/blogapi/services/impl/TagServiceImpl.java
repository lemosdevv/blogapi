package io.mateus.blogapi.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.mateus.blogapi.exception.ResourceNotFoundException;
import io.mateus.blogapi.exception.UnauthorizedException;
import io.mateus.blogapi.model.Tag;
import io.mateus.blogapi.model.role.RoleName;
import io.mateus.blogapi.payload.response.ApiResponse;
import io.mateus.blogapi.payload.response.PagedResponse;
import io.mateus.blogapi.repositories.TagRepository;
import io.mateus.blogapi.security.UserPrincipal;
import io.mateus.blogapi.services.TagService;
import io.mateus.blogapi.utils.AppUtils;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    // Método para listar todas as tags com paginação
    @Override
    public PagedResponse<Tag> getAllTags(int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        Page<Tag> tags = tagRepository.findAll(pageable);

        List<Tag> content = tags.getNumberOfElements() == 0 ? List.of() : tags.getContent();

        return new PagedResponse<>(content, tags.getNumber(), tags.getSize(), tags.getTotalElements(),
                tags.getTotalPages(), tags.isLast());
    }

    // Método para buscar uma tag pelo ID
    @Override
    public Tag getTag(Long id) {
        return tagRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tag", "id", id));
    }

    // Método para adicionar uma tag
    @Override
    public Tag addTag(Tag tag, UserPrincipal currentUser) {
        return tagRepository.save(tag);
    }

    // Método para atualizar uma tag
    @Override
    public Tag updateTag(Long id, Tag newTag, UserPrincipal currentUser) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tag", "id", id));
        if (tag.getCreatedBy().equals(currentUser.getId()) || currentUser.getAuthorities()
                .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.name()))) {
            tag.setName(newTag.getName());
            return tagRepository.save(tag);
        }
        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You do not have permission to update this tag.");

        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ApiResponse deleteTag(Long id, UserPrincipal currentUser) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tag", "id", id));
        if (tag.getCreatedBy().equals(currentUser.getId()) || currentUser.getAuthorities()
                .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.name()))) {
            tagRepository.deleteById(id);
            return new ApiResponse(Boolean.TRUE, "Tag deleted successfully.");
        }
        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You do not have permission to update this tag.");

        throw new UnauthorizedException(apiResponse);
    }

}
