package com.github.movierating.entity;

import jakarta.persistence.*;
import lombok.Setter;

@MappedSuperclass
@Setter
public abstract class CommonEntity {

    protected Long id;
    protected Long version;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEFAULT_GEN")
    public Long getId() {
        return id;
    }

    @Version
    @Column(name = "VERSION")
    public Long getVersion() {
        return version;
    }
}
