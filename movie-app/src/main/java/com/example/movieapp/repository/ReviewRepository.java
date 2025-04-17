package com.example.movieapp.repository;

import com.example.movieapp.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Page<Review> findByMovie_Id(Integer movieId, Pageable pageable);

    @Modifying
    @Query("DELETE FROM Review r WHERE r.movie.id = :movieId")
    void deleteByMovieId(@Param("movieId") Integer movieId);

}