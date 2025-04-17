package com.example.movieapp.api;

import com.example.movieapp.entity.Favorite;
import com.example.movieapp.entity.Movie;
import com.example.movieapp.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    // Lấy danh sách phim yêu thích của người dùng với phân trang
    @GetMapping
    public ResponseEntity<Page<Favorite>> getFavorites(
            @RequestParam Integer userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        Page<Favorite> favorites = favoriteService.getFavoritesByUserId(userId, page, pageSize);
        return ResponseEntity.ok(favorites);
    }



    // Lấy danh sách phim yêu thích của người dùng dưới dạng Movie với phân trang
    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getFavoriteMovies(
            @RequestParam Integer userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        Page<Movie> moviePage = favoriteService.getUserFavoriteMovies(userId, page, pageSize);
        List<Movie> movies = moviePage.getContent(); // Chuyển đổi từ Page<Movie> sang List<Movie>

        return ResponseEntity.ok(movies);
    }




    // Thêm phim vào danh sách yêu thích
    @PostMapping
    public ResponseEntity<Favorite> addFavorite(
            @RequestParam Integer userId,
            @RequestParam Integer movieId) {
        Favorite favorite = favoriteService.addFavorite(userId, movieId);
        return ResponseEntity.ok(favorite);
    }

    // Xóa một phim yêu thích của người dùng
    @DeleteMapping
    public ResponseEntity<String> removeFavorite(
            @RequestParam Integer userId,
            @RequestParam Integer movieId) {
        favoriteService.removeFavorite(userId, movieId);
        return ResponseEntity.ok("Đã xóa phim yêu thích.");
    }

    // Xóa toàn bộ phim yêu thích của người dùng
    @DeleteMapping("/{userId}/all")
    public ResponseEntity<String> removeAllFavorites(@PathVariable Integer userId) {
        favoriteService.removeAllFavorites(userId);
        return ResponseEntity.ok("Đã xóa toàn bộ phim yêu thích.");
    }
}
