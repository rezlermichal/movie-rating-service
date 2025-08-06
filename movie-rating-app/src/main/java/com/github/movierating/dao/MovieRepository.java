package com.github.movierating.dao;

import com.github.movierating.dto.MovieDto;
import com.github.movierating.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    /*
select m.*, r.avg_rating
from movie m
join (
	select r.movie_id, avg(r.rating) as avg_rating
	from public.rating r
	group by r.movie_id
) r on r.movie_id = m.id
order by r.avg_rating desc;
     */

    @Query(value = """
            select m.id, m.name, r.avgRating
            from movie m
            join (
            	select r.movie_id, avg(r.rating) as avgRating
            	from rating r
            	group by r.movie_id
            ) r on r.movie_id = m.id
    """,
    countQuery = """
            select count(*)
            from rating r
            group by r.movie_id
    """,
    nativeQuery = true)
    Page<MovieDto> findAllWithAvgRating(Pageable pageable);

}
