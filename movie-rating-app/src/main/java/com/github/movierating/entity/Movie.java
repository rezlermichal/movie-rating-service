package com.github.movierating.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.Setter;

@Entity(name = "MOVIE")
@Setter
public class Movie {

    private Long id;
    private Long version;
    private String name;

    @Id
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    @Version
    @Column(name = "VERSION")
    public Long getVersion() {
        return version;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }
}
