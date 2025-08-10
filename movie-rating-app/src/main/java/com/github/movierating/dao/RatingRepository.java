package com.github.movierating.dao;

import com.github.movierating.entity.Movie;
import com.github.movierating.entity.Rating;
import com.github.movierating.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    boolean existsRatingByMovieAndUser(Movie movie, User user);

}
