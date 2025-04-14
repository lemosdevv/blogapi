package io.mateus.blogapi.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.mateus.blogapi.model.audit.UserDateAudit;
import io.mateus.blogapi.model.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "albums", uniqueConstraints = {@UniqueConstraint(columnNames = {"title",}) })
public class Album extends UserDateAudit {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photo;

    public String getEmail() {
        return user.getEmail();
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }
    
    public List<Photo> getPhoto() {
        return this.photo == null ? null : new ArrayList<>(this.photo);
    }

    public void setPhoto(List<Photo> photo) {
        if (photo == null){
            this.photo = null;
        } else {
            this.photo = Collections.unmodifiableList(photo);
        }
    }

    @Override
    public Long getCreatedBy() {
        return 0L;
    }
}
