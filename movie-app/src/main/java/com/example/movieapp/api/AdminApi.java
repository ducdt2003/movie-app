package com.example.movieapp.api;

import com.example.movieapp.entity.Movie;
import com.example.movieapp.model.enums.MovieType;
import com.example.movieapp.model.request.MovieRequest;
import com.example.movieapp.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/admin/movies")
public class AdminApi {

    @Autowired
    private MovieService movieService;

  // Lấy danh sách phim - câu 1
    @GetMapping
    public Page<Movie> getMovies(@RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                 @RequestParam(required = false) MovieType type) {
        if (type != null) {
            return movieService.findByType(type, true, page, pageSize);
        }
        return movieService.findAllMovies(page, pageSize);
    }

    // API tạo phim mới - câu 2
    @PostMapping
    public ResponseEntity<Movie> createMovie(@Valid @RequestBody MovieRequest movieRequest) {
        Movie newMovie = movieService.createMovie(movieRequest);
        return ResponseEntity.ok(newMovie);
    }

    // cập nhật phim  - câu 3
    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Integer id, @Valid @RequestBody MovieRequest movieRequest) {
        Movie updatedMovie = movieService.updateMovie(id, movieRequest);
        return ResponseEntity.ok(updatedMovie);
    }


    // xáo phim - cau 4
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable Integer id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok("Xóa phim thành công với ID: " + id);
    }



}

