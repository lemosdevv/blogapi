package io.mateus.blogapi.model.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.mateus.blogapi.model.Album;
import io.mateus.blogapi.model.Comment;
import io.mateus.blogapi.model.Post;
import io.mateus.blogapi.model.Todo;
import io.mateus.blogapi.model.audit.DateAudit;
import io.mateus.blogapi.model.role.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username",}),
        @UniqueConstraint(columnNames = {"email",})})
public class User extends DateAudit{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Column(name = "first_name")
    @Size(min = 3, max = 40)
    private String firstName;

    @NotBlank
    @Column(name = "last_name")
    @Size(min = 3, max = 40)
    private String lastName;

    @NotBlank
    @Column(name = "username")
    @Size(min = 3, max = 15) 
    private String username;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password")
    @Size(min = 3, max = 100) 
    private String password;

    @NotBlank
    @NaturalId
    @Size(max = 40)
    @Column(name = "email")
    @Email
    private String email;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "address_id")
	private Address address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "website")
    private String website;

    @ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private List<Role> roles;

	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Todo> todos;

	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Album> albums;

	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Post> posts;

	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "company_id")
	private Company company;

    public User(String firstName, String lastName, String username, String email, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.password = password;
	}



	public List<Todo> getTodos() {

		return todos == null ? null : new ArrayList<>(todos);
	}

	public void setTodos(List<Todo> todos) {

		if (todos == null) {
			this.todos = null;
		} else {
			this.todos = Collections.unmodifiableList(todos);
		}
	}

	public List<Album> getAlbums() {

		return albums == null ? null : new ArrayList<>(albums);
	}

	public void setAlbums(List<Album> albums) {

		if (albums == null) {
			this.albums = null;
		} else {
			this.albums = Collections.unmodifiableList(albums);
		}
	}


	public List<Post> getPosts() {

		return posts == null ? null : new ArrayList<>(posts);
	}

	public void setPosts(List<Post> posts) {

		if (posts == null) {
			this.posts = null;
		} else {
			this.posts = Collections.unmodifiableList(posts);
		}
	}

	public List<Role> getRoles() {

		return roles == null ? null : new ArrayList<>(roles);
	}

	public void setRoles(List<Role> roles) {

		if (roles == null) {
			this.roles = null;
		} else {
			this.roles = Collections.unmodifiableList(roles);
		}
	}

	public List<Comment> getComments() {

		return comments == null ? null : new ArrayList<>(comments);
	}

	public void setComments(List<Comment> comments) {

		if (comments == null) {
			this.comments = null;
		} else {
			this.comments = Collections.unmodifiableList(comments);
		}
	}
}
