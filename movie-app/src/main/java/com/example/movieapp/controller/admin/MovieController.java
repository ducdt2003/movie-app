package com.example.movieapp.controller.admin;

import com.example.movieapp.entity.Movie;
import com.example.movieapp.entity.Actor; // Import Actor
import com.example.movieapp.entity.Director; // Import Director
import com.example.movieapp.entity.Genre;
import com.example.movieapp.model.enums.MovieType;
import com.example.movieapp.model.request.MovieRequest;
import com.example.movieapp.repository.ActorRepository;
import com.example.movieapp.repository.CountryRepository;
import com.example.movieapp.repository.DirectorRepository;
import com.example.movieapp.repository.GenreRepository;
import com.example.movieapp.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

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
        Page<Movie> moviePage = movieService.findAllMovies(page, size);
        model.addAttribute("movies", moviePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", moviePage.getTotalPages());
        model.addAttribute("totalItems", moviePage.getTotalElements());
        return "admin/movie/blog-index";
    }

    @GetMapping("/create")
    public String getCreatePage(Model model) {
        model.addAttribute("movie", new MovieRequest());
        model.addAttribute("countries", countryRepository.findAll());
        model.addAttribute("actors", actorRepository.findAll());
        model.addAttribute("directors", directorRepository.findAll());
        model.addAttribute("genres", genreRepository.findAll());
        model.addAttribute("movieTypes", MovieType.values());
        return "admin/movie/blog-create";
    }

    @PostMapping("/create")
    public String createMovie(@ModelAttribute("movie") @Valid MovieRequest movieRequest, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println("Validation errors: " + result.getAllErrors());
            model.addAttribute("countries", countryRepository.findAll());
            model.addAttribute("actors", actorRepository.findAll());
            model.addAttribute("directors", directorRepository.findAll());
            model.addAttribute("genres", genreRepository.findAll());
            model.addAttribute("movieTypes", MovieType.values());
            return "admin/movie/blog-create";
        }
        Movie createdMovie = movieService.createMovie(movieRequest);
        return "redirect:/admin/movies/" + createdMovie.getId();
    }

    @GetMapping("/{id}")
    public String getMovieDetail(@PathVariable Integer id, Model model) {
        Movie movie = movieService.findMovieById(id);
        model.addAttribute("movie", movie);
        return "admin/movie/blog-detail";
    }

    @GetMapping("/edit/{id}")
    public String getEditPage(@PathVariable Integer id, Model model) {
        Movie movie = movieService.findMovieById(id);
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setName(movie.getName());
        movieRequest.setTrailerUrl(movie.getTrailer());
        movieRequest.setDescription(movie.getDescription());
        movieRequest.setReleaseYear(movie.getReleaseYear());
        movieRequest.setType(movie.getType());
        movieRequest.setStatus(movie.getStatus());
        movieRequest.setCountryId(movie.getCountry() != null ? movie.getCountry().getId() : null);
        movieRequest.setGenreIds(movie.getGenres() != null ? movie.getGenres().stream().map(Genre::getId).collect(Collectors.toList()) : null);
        movieRequest.setActorIds(movie.getActors() != null ? movie.getActors().stream().map(Actor::getId).collect(Collectors.toList()) : null);
        movieRequest.setDirectorIds(movie.getDirectors() != null ? movie.getDirectors().stream().map(Director::getId).collect(Collectors.toList()) : null);

        model.addAttribute("movie", movieRequest);
        model.addAttribute("movieId", id); // Thêm movieId cho th:action
        model.addAttribute("countries", countryRepository.findAll());
        model.addAttribute("actors", actorRepository.findAll());
        model.addAttribute("directors", directorRepository.findAll());
        model.addAttribute("genres", genreRepository.findAll());
        model.addAttribute("movieTypes", MovieType.values());
        return "admin/movie/blog-edit";
    }

    @PostMapping("/edit/{id}")
    public String updateMovie(@PathVariable Integer id, @ModelAttribute("movie") @Valid MovieRequest movieRequest, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println("Validation errors: " + result.getAllErrors());
            model.addAttribute("movieId", id);
            model.addAttribute("countries", countryRepository.findAll());
            model.addAttribute("actors", actorRepository.findAll());
            model.addAttribute("directors", directorRepository.findAll());
            model.addAttribute("genres", genreRepository.findAll());
            model.addAttribute("movieTypes", MovieType.values());
            return "admin/movie/blog-edit";
        }
        System.out.println("Updating movie with ID: " + id + ", Request: " + movieRequest);
        movieService.updateMovie(id, movieRequest);
        return "redirect:/admin/movies/" + id;
    }
}
