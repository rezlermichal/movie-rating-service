package com.github.movierating.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import lombok.Setter;

@Entity(name = "MOVIE")
@Setter
@SequenceGenerator(name = "DEFAULT_GEN", sequenceName = "MOVIE_SEQUENCE")
public class Movie extends CommonEntity {

    private String name;

    @Column(name = "NAME")
    public String getName() {
        return name;
    }
}
