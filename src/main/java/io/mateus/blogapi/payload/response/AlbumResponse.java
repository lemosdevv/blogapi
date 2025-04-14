package io.mateus.blogapi.payload.response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.mateus.blogapi.model.Photo;
import io.mateus.blogapi.model.user.User;
import io.mateus.blogapi.payload.UserDateAuditPayload;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(Include.NON_NULL)
public class AlbumResponse extends UserDateAuditPayload{
    private Long id;

    private String title;

    private User user;

    private List<Photo> photo;

    public List<Photo> getPhoto() {

		return photo == null ? null : new ArrayList<>(photo);
	}

	public void setPhoto(List<Photo> photo) {

		if (photo == null) {
			this.photo = null;
		} else {
			this.photo = Collections.unmodifiableList(photo);
		}
	}

}
