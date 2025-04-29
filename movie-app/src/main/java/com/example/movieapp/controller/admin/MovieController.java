/*
package com.example.movieapp.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/movies")
public class MovieController {

    @GetMapping
    public String getIndexPage(){
        return "admin/movie/blog-index";
    }

    @GetMapping ("/create")
    public String getCreate(){
        return "admin/movie/blog-create";
    }
    @GetMapping("/{id}")
    public String getDetail(){
        return "admin/movie/blog-detail";
    }
}
*/
package com.example.movieapp.controller.admin;

import com.example.movieapp.entity.Movie;
import com.example.movieapp.model.enums.MovieType;
import com.example.movieapp.repository.ActorRepository;
import com.example.movieapp.repository.CountryRepository;
import com.example.movieapp.repository.DirectorRepository;
import com.example.movieapp.repository.GenreRepository;
import com.example.movieapp.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Controller
@RequestMapping("/admin/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final CountryRepository countryRepository;
    private final ActorRepository actorRepository;
    private final DirectorRepository directorRepository;
    private final GenreRepository genreRepository;

    @GetMapping
    public String getIndexPage(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int size,
                               Model model) {
        // Lấy danh sách phim với phân trang
        Page<Movie> moviePage = movieService.findAllMovies(page, size);

        // Truyền dữ liệu vào model để hiển thị trên giao diện
        model.addAttribute("movies", moviePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", moviePage.getTotalPages());
        model.addAttribute("totalItems", moviePage.getTotalElements());

        return "admin/movie/blog-index";
    }

    @GetMapping("/create")
    public String getCreatePage(Model model) {
        // Truyền danh sách dữ liệu từ DB vào model
        model.addAttribute("countries", countryRepository.findAll());
        model.addAttribute("actors", actorRepository.findAll());
        model.addAttribute("directors", directorRepository.findAll());
        model.addAttribute("genres", genreRepository.findAll());
        // Truyền danh sách MovieType (enum) để hiển thị loại phim
        model.addAttribute("movieTypes", MovieType.values());
        return "admin/movie/blog-create";
    }

    @GetMapping("/{id}")
    public String getMovieDetail(@PathVariable Integer id, Model model) {
        Movie movie = movieService.findMovieById(id); // <- Gọi hàm mới
        model.addAttribute("movie", movie);
        return "admin/movie/blog-detail";
    }
}

