package com.example.movieapp.service;

import com.example.movieapp.entity.Favorite;
import com.example.movieapp.entity.Movie;
import com.example.movieapp.entity.User;
import com.example.movieapp.repository.FavoriteRepository;
import com.example.movieapp.repository.MovieRepository;
import com.example.movieapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    // Lấy danh sách các phim yêu thích của người dùng với phân trang
    public Page<Favorite> getFavoritesByUserId(Integer userId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return favoriteRepository.findByUserId(userId, pageable);
    }

    // Lấy danh sách các movie yêu thích của người dùng
    /*public List<Movie> getUserFavoriteMovies(Integer userId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Favorite> favoritesPage = favoriteRepository.findByUserId(userId, pageable);
        return favoritesPage.stream().map(Favorite::getMovie).collect(Collectors.toList());
    }*/

    public Page<Movie> getUserFavoriteMovies(Integer userId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Favorite> favoritesPage = favoriteRepository.findByUserId(userId, pageable);
        return favoritesPage.map(Favorite::getMovie);
    }


    // Thêm phim vào danh sách yêu thích
    public Favorite addFavorite(Integer userId, Integer movieId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Movie> movieOpt = movieRepository.findById(movieId);

        if (userOpt.isPresent() && movieOpt.isPresent()) {
            Favorite favorite = Favorite.builder()
                    .user(userOpt.get())
                    .movie(movieOpt.get())
                    .build();
            return favoriteRepository.save(favorite);
        }
        throw new RuntimeException("User or Movie not found");
    }

    // Xóa một phim yêu thích của người dùng
    @Transactional
    public void removeFavorite(Integer userId, Integer movieId) {
        favoriteRepository.deleteByUserIdAndMovieId(userId, movieId);
    }

    // Xóa tất cả các phim yêu thích của người dùng
    public void removeAllFavorites(Integer userId) {
        favoriteRepository.deleteByUserId(userId);
    }


}
