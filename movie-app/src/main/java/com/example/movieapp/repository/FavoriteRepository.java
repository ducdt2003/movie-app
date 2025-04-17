package com.example.movieapp.repository;

import com.example.movieapp.entity.Favorite;
import com.example.movieapp.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {

    // Lấy các Favorite của user với phân trang
    Page<Favorite> findByUserId(Integer userId, Pageable pageable);

    // Lấy danh sách các Movie yêu thích của user
    @Query("SELECT f.movie FROM Favorite f WHERE f.user.id = :userId")
    List<Movie> findMoviesByUserId(@Param("userId") Integer userId);

    // Xóa một phim yêu thích của người dùng
    @Modifying
    @Transactional
    @Query("DELETE FROM Favorite f WHERE f.user.id = :userId AND f.movie.id = :movieId")
    void deleteByUserIdAndMovieId(@Param("userId") Integer userId, @Param("movieId") Integer movieId);

    // Xóa toàn bộ phim yêu thích của người dùng
    @Modifying
    @Transactional
    @Query("DELETE FROM Favorite f WHERE f.user.id = :userId")
    void deleteByUserId(@Param("userId") Integer userId);

}
