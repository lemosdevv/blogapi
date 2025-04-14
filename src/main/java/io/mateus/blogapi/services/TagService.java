package io.mateus.blogapi.services;

import io.mateus.blogapi.model.Tag;
import io.mateus.blogapi.payload.response.ApiResponse;
import io.mateus.blogapi.payload.response.PagedResponse;
import io.mateus.blogapi.security.UserPrincipal;

public interface TagService {

    PagedResponse<Tag> getAllTags(int page, int size);

    Tag getTag(Long id);

    Tag addTag(Tag tag, UserPrincipal currentUser);

    Tag updateTag(Long id, Tag newTag, UserPrincipal currentUser);

    ApiResponse deleteTag(Long id, UserPrincipal currentUser);

}
