package io.mateus.blogapi.model.audit;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
@JsonIgnoreProperties(
    value = {"createdAt", "updatedAt"},
    allowGetters = true
)
public abstract class UserDateAudit extends DateAudit {
    private static final long serialVersionUID = 1L;

    @CreatedBy
    @Column(updatable = false)
    private Long createdBy;

    @LastModifiedBy
    private Long updatedBy;

    @JsonIgnore
    public Long getCreatedBy() {
        return createdBy;
    }

    @JsonIgnore
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy; 
    }

    @JsonIgnore
    public Long getUpdatedBy() {
        return updatedBy;
    }

    @JsonIgnore
    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy; 
    }
}