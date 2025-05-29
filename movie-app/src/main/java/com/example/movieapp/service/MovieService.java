package com.example.movieapp.service;

import com.example.movieapp.entity.*;
import com.example.movieapp.exception.BadRequestException;
import com.example.movieapp.exception.NotFoundException;
import com.example.movieapp.model.enums.MovieType;
import com.example.movieapp.model.request.MovieRequest;
import com.example.movieapp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final CountryRepository countryRepository;
    private final GenreRepository genreRepository;
    private final ActorRepository actorRepository;
    private final DirectorRepository directorRepository;
    private final ReviewRepository reviewRepository;
    private final EpisodeRepository episodeRepository;
    private final CloudinaryService cloudinaryService;

    public List<Movie> findHotMovie(Boolean status, Integer limit) {
        return movieRepository.findHotMovie(status, limit);
    }

    public Page<Movie> findByType(MovieType type, Boolean status, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("publishedAt").descending());
        Page<Movie> moviePage = movieRepository.findByTypeAndStatus(type, status, pageable);
        return moviePage;
    }

    public Movie findMovieDetails(Integer id, String slug) {
        return movieRepository.findByIdAndSlugAndStatus(id, slug, true);
    }
    public Movie findMovieById(Integer id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phim với ID: " + id));
    }


    // phim liên quan
    public List<Movie> findRelatedMovies(Integer movieId) {
        // 🔹 Tìm phim theo ID
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phim"));

        // 🔹 Lấy danh sách phim có cùng thể loại nhưng loại bỏ phim hiện tại
        return movieRepository.findByGenresIn(movie.getGenres()).stream()
                .filter(m -> !m.getId().equals(movieId)) // Loại bỏ chính phim hiện tại
                .limit(6) // Giới hạn 6 phim liên quan
                .collect(Collectors.toList());
    }


    // lấy danh sách phim Admin- câu 1
    public Page<Movie> findAllMovies(Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("publishedAt").descending());
        return movieRepository.findAll(pageable);
    }


    // tạo mới phim - câu 2
    public Country fetchCountryById(Integer countryId) {
        return countryRepository.findById(countryId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quốc gia với ID: " + countryId));
    }

    public List<Genre> fetchGenresByIds(List<Integer> genreIds) {
        return genreIds != null ? genreRepository.findAllById(genreIds) : new ArrayList<>();
    }

    public List<Actor> fetchActorsByIds(List<Integer> actorIds) {
        return actorIds != null ? actorRepository.findAllById(actorIds) : new ArrayList<>();
    }

    public List<Director> fetchDirectorsByIds(List<Integer> directorIds) {
        return directorIds != null ? directorRepository.findAllById(directorIds) : new ArrayList<>();
    }

    public Movie createMovie(MovieRequest movieRequest) {
        Movie movie = Movie.builder()
                .name(movieRequest.getName())
                .trailer(movieRequest.getTrailerUrl()) // 🔹 Đảm bảo đúng tên trường
                .description(movieRequest.getDescription())
                .releaseYear(movieRequest.getReleaseYear())
                .type(movieRequest.getType())
                .status(movieRequest.getStatus())
                .country(fetchCountryById(movieRequest.getCountryId())) // 🔹 Chuyển từ ID thành đối tượng
                .genres(fetchGenresByIds(movieRequest.getGenreIds()))
                .actors(fetchActorsByIds(movieRequest.getActorIds()))
                .directors(fetchDirectorsByIds(movieRequest.getDirectorIds()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return movieRepository.save(movie);
    }


    // Cập nhật phim - câu 3
    public Movie updateMovie(Integer id, MovieRequest movieRequest) {
        System.out.println("MovieRequest: " + movieRequest);
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phim với ID: " + id));

        movie.setName(movieRequest.getName());
        movie.setTrailer(movieRequest.getTrailerUrl());
        movie.setDescription(movieRequest.getDescription());
        movie.setReleaseYear(movieRequest.getReleaseYear());
        movie.setType(movieRequest.getType());
        movie.setStatus(movieRequest.getStatus());
        movie.setCountry(fetchCountryById(movieRequest.getCountryId()));
        movie.setGenres(fetchGenresByIds(movieRequest.getGenreIds()));
        movie.setActors(fetchActorsByIds(movieRequest.getActorIds()));
        movie.setDirectors(fetchDirectorsByIds(movieRequest.getDirectorIds()));
        movie.setUpdatedAt(LocalDateTime.now());

        Movie savedMovie = movieRepository.save(movie);
        System.out.println("Saved movie: " + savedMovie.getName());
        return savedMovie;
    }


    // xoa phim câu 4 ( phụ thuộc vào reviews và tập pjhm)

    @Transactional
    public void deleteMovie(Integer id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phim với ID: " + id));

        // Xóa tất cả dữ liệu liên quan trước khi xóa phim
        reviewRepository.deleteByMovieId(id);
        episodeRepository.deleteByMovieId(id);

        // Sau khi xóa dữ liệu liên quan, xóa phim
        movieRepository.delete(movie);
    }

    public Map uploadThumbnail(Integer id, MultipartFile file) {
        Movie movie = movieRepository.findById(id)

                .orElseThrow(() -> new NotFoundException("Không tìm thấy phim với id " + id));
        try {
            Map map = cloudinaryService.uploadFile(file, "file_java_27_28");
            String url = map.get("url").toString();
            movie.setThumbnail(url);
            movie.setUpdatedAt(LocalDateTime.now());
            movieRepository.save(movie);
            return Map.of( "url", url);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }





}
