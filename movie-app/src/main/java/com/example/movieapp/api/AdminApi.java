package com.example.movieapp.api;

import com.example.movieapp.entity.Movie;
import com.example.movieapp.model.enums.MovieType;
import com.example.movieapp.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/admin/movies")
public class AdminApi {

    @Autowired
    private MovieService movieService;

//    Lấy danh sách phim
    @GetMapping
    public Page<Movie> getMovies(@RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                 @RequestParam(required = false) MovieType type) {
        if (type != null) {
            return movieService.findByType(type, true, page, pageSize);
        }
        return movieService.findAllMovies(page, pageSize);
    }

}

