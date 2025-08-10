package com.github.movierating.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "RATING")
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "DEFAULT_GEN", sequenceName = "RATING_SEQUENCE")
public class Rating extends CommonEntity {

    private int rating;
    private User user;
    private Movie movie;
    private LocalDateTime ratedAt;

    @Column(name = "RATING")
    public int getRating() {
        return rating;
    }

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    public User getUser() {
        return user;
    }

    @ManyToOne
    @JoinColumn(name = "MOVIE_ID")
    public Movie getMovie() {
        return movie;
    }

    @Column(name = "RATED_AT")
    public LocalDateTime getRatedAt() {
        return ratedAt;
    }
}
