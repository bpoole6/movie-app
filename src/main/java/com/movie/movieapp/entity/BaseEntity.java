package com.movie.movieapp.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private ZonedDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private ZonedDateTime updatedAt;
    @Version
    @Column(name = "version", nullable = false)
    private long version;


    public abstract Long getId();

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * {@link BaseEntity}s can be tested for equality, however, two <b>unsaved</b> instances of the same entity will be
     * considered equal because their ids are both zero.
     *
     * @param other
     * @return
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BaseEntity)) {
            return false;
        }
        BaseEntity otherEntity = (BaseEntity) other;
        return this.getId() == otherEntity.getId();
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.getId());
    }
}