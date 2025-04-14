package io.mateus.blogapi.payload.request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.mateus.blogapi.model.Photo;
import io.mateus.blogapi.model.user.User;
import io.mateus.blogapi.payload.UserDateAuditPayload;
import lombok.Data;

@Data
public class AlbumRequest extends UserDateAuditPayload{

    private Long id;

    private String title;

    private User user;

    private List<Photo> photo;

    public List<Photo> getPhoto(){

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
