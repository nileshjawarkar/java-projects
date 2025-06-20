package com.nnj.learn.jee.entity;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(name = "created_at", insertable = true, updatable = false)
    OffsetDateTime createdAt;

    @Column(name = "modified_at", insertable = true, updatable = true)
    OffsetDateTime modifiedAt;

    @PrePersist
    public void prePersist() {
        createdAt = OffsetDateTime.now(ZoneId.of("UTC"));
        modifiedAt = OffsetDateTime.now(ZoneId.of("UTC"));
    }

    @PreUpdate
    public void preUpdate() {
        modifiedAt = OffsetDateTime.now(ZoneId.of("UTC"));
    }

    public BaseEntity() {
    }

    public BaseEntity(final UUID id, final OffsetDateTime createdAt, final OffsetDateTime modifiedAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(final OffsetDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
