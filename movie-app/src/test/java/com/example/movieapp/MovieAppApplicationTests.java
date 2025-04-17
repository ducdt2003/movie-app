package com.example.movieapp;

import com.example.movieapp.entity.*;
import com.example.movieapp.model.enums.MovieType;
import com.example.movieapp.model.enums.UserRole;
import com.example.movieapp.repository.*;
import com.github.javafaker.Faker;
import com.github.slugify.Slugify;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest
class MovieAppApplicationTests {

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void save_countries() {
        Faker faker = new Faker();
        Slugify slugify = Slugify.builder().build();

        for (int i = 0; i < 10; i++) {
            String name = faker.country().name();
            String uniqueSlug = slugify.slugify(name) + "-" + UUID.randomUUID().toString().substring(0, 8);

            // Kiểm tra xem quốc gia đã tồn tại chưa
            if (!countryRepository.existsByName(name) && !countryRepository.existsBySlug(uniqueSlug)) {
                Country country = Country.builder()
                        .name(name)
                        .slug(uniqueSlug)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

                try {
                    countryRepository.save(country);
                } catch (DataIntegrityViolationException e) {
                    System.out.println("Lỗi: Quốc gia '" + name + "' đã tồn tại!");
                }
            }
        }
    }

    @Test
    void save_favorites() {
        // Tạo dữ liệu giả cho Movie
        Movie movie = Movie.builder()
                .name("Inception")
                .description("A mind-bending thriller")
                .releaseYear(2010)
                .build();
        movie = movieRepository.save(movie);

        // Đảm bảo email là duy nhất
        String uniqueEmail = "john.doe" + System.currentTimeMillis() + "@example.com";

        // Tạo một người dùng với email duy nhất
        User user = User.builder()
                .username("john_doe")
                .email(uniqueEmail)  // Sử dụng email duy nhất
                .password("password123")
                .build();
        user = userRepository.save(user);

        // Tạo một bản ghi yêu thích
        Favorite favorite = Favorite.builder()
                .movie(movie)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        Favorite savedFavorite = favoriteRepository.save(favorite);

        // Kiểm tra xem favorite đã được lưu thành công hay chưa
        assert(savedFavorite != null);
    }

    @Test
    void saveGenres() {
        Faker faker = new Faker();
        Slugify slugify = Slugify.builder().build();

        for (int i = 0; i < 10; i++) {
            String name = faker.leagueOfLegends().champion();
            String slug = slugify.slugify(name);

            // Kiểm tra xem thể loại đã tồn tại chưa
            Optional<Genre> existingGenre = genreRepository.findBySlug(slug);
            if (existingGenre.isPresent()) {
                // Nếu tồn tại, thêm một định danh duy nhất vào slug
                slug += "-" + UUID.randomUUID().toString().substring(0, 5);
            }

            Genre genre = Genre.builder()
                    .name(name)
                    .slug(slug)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            genreRepository.save(genre);
        }
    }

    @Test
    void save_actors() {
        Faker faker = new Faker();
        Slugify slugify = Slugify.builder().build();
        for (int i = 0; i < 100; i++) {
            String name = faker.name().fullName();
            Actor actor = Actor.builder()
                    .name(name)
                    .slug(slugify.slugify(name))
                    .avatar("https://placehold.co/600x400?text=" + name.substring(0, 1).toUpperCase())
                    .bio(faker.lorem().paragraph())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            actorRepository.save(actor);
        }
    }

    @Test
    void save_directors() {
        Faker faker = new Faker();
        Slugify slugify = Slugify.builder().build();
        for (int i = 0; i < 30; i++) {
            String name = faker.name().fullName();
            Director director = Director.builder()
                    .name(name)
                    .slug(slugify.slugify(name))
                    .avatar("https://placehold.co/600x400?text=" + name.substring(0, 1).toUpperCase())
                    .bio(faker.lorem().paragraph())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            directorRepository.save(director);
        }
    }

    @Test
    void save_users() {
        Faker faker = new Faker();
        for (int i = 0; i < 50; i++) {
            String displayName = faker.name().fullName();
            User user = User.builder()
                    .username(faker.name().username())
                    .displayName(displayName)
                    .email(faker.internet().emailAddress())
                    .avatar("https://placehold.co/600x400?text=" + displayName.substring(0, 1).toUpperCase())
                    .phone(faker.phoneNumber().cellPhone())
                    .password("123")
                    .role(i < 2 ? UserRole.ADMIN : UserRole.USER)
                    .isEnabled(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            userRepository.save(user);
        }
    }

    @Test
    void update_user_password() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            String password = user.getPassword();
            String newPassword = passwordEncoder.encode(password);
            user.setPassword(newPassword);
            userRepository.save(user);
        }
    }

    @Test
    void save_posts() {
        Faker faker = new Faker();
        Slugify slugify = Slugify.builder().build();

        List<User> users = userRepository.findByRole(UserRole.ADMIN);

        for (int i = 0; i < 50; i++) {
            String title = faker.book().title();
            Boolean status = faker.bool().bool();
            User rdUser = users.get(faker.number().numberBetween(0, users.size()));
            Post post = Post.builder()
                    .title(title)
                    .slug(slugify.slugify(title))
                    .content(faker.lorem().paragraph(50))
                    .description(faker.lorem().paragraph())
                    .thumbnail("https://placehold.co/600x400?text=" + title.substring(0, 1).toUpperCase())
                    .status(status)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .publishedAt(status ? LocalDateTime.now() : null)
                    .user(rdUser)
                    .build();
            postRepository.save(post);
        }
    }

    @Test
    void save_movies() {
        Faker faker = new Faker();
        Slugify slugify = Slugify.builder().build();
        Random rd = new Random();

        List<Country> countries = countryRepository.findAll();
        List<Genre> genres = genreRepository.findAll();
        List<Actor> actors = actorRepository.findAll();
        List<Director> directors = directorRepository.findAll();

        for (int i = 0; i < 150; i++) {
            // Random a country
            Country rdCountry = countries.get(rd.nextInt(countries.size()));

            // Random list genres
            List<Genre> rdGenres = new ArrayList<>();
            // Random 2 -> 4 genres
            for (int j = 0; j < rd.nextInt(3) + 2; j++) {
                Genre rdGenre = genres.get(rd.nextInt(genres.size()));
                if (!rdGenres.contains(rdGenre)) {
                    rdGenres.add(rdGenre);
                }
            }

            // Random list actors
            List<Actor> rdActors = new ArrayList<>();
            // Random 4 -> 7 actors
            for (int j = 0; j < rd.nextInt(4) + 4; j++) {
                Actor rdActor = actors.get(rd.nextInt(actors.size()));
                if (!rdActors.contains(rdActor)) {
                    rdActors.add(rdActor);
                }
            }

            // Random list directors
            List<Director> rdDirectors = new ArrayList<>();
            // Random 1 -> 2 directors
            for (int j = 0; j < rd.nextInt(2) + 1; j++) {
                Director rdDirector = directors.get(rd.nextInt(directors.size()));
                if (!rdDirectors.contains(rdDirector)) {
                    rdDirectors.add(rdDirector);
                }
            }

            // Tao entity
            String name = faker.funnyName().name();
            String thumbnail = "https://placehold.co/600x400?text=" + name.substring(0, 1).toUpperCase();
            Boolean status = faker.bool().bool();

            int rdIndexType = rd.nextInt(MovieType.values().length);
            MovieType type = MovieType.values()[rdIndexType];

            Movie movie = Movie.builder()
                    .name(name)
                    .slug(slugify.slugify(name))
                    .description(faker.lorem().paragraph())
                    .thumbnail(thumbnail)
                    .releaseYear(faker.number().numberBetween(1990, 2021))
                    .trailer("https://www.youtube.com/embed/W_0AMP9yO1w?si=JcCeGorHalCHKCPl")
                    .status(status)
                    .rating(faker.number().randomDouble(1, 5, 10))
                    .type(type)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .publishedAt(status ? LocalDateTime.now() : null)
                    .country(rdCountry)
                    .genres(rdGenres)
                    .actors(rdActors)
                    .directors(rdDirectors)
                    .build();

            // Luu vao DB
            movieRepository.save(movie);
        }
    }

    @Test
    void save_episodes() {
        Faker faker = new Faker();
        List<Movie> movies = movieRepository.findAll();

        if (movies.isEmpty()) {
            System.out.println("Không có dữ liệu phim trong hệ thống.");
            return;
        }

        for (Movie movie : movies) {
            // Kiểm tra `movie` và `movie.getType()` để tránh lỗi NullPointerException
            if (movie == null || movie.getType() == null) {
                continue;
            }

            if (movie.getType().equals(MovieType.PHIM_BO)) {
                // random 5 -> 10 episodes
                for (int i = 0; i < faker.number().numberBetween(5, 11); i++) {
                    Episode episode = Episode.builder()
                            .name("Tập " + (i + 1))
                            .duration(faker.number().numberBetween(40, 60))
                            .displayOrder(i + 1)
                            .videoUrl("https://www.youtube.com/embed/W_0AMP9yO1w?si=JcCeGorHalCHKCPl")
                            .status(true)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .publishedAt(LocalDateTime.now())
                            .movie(movie)
                            .build();
                    episodeRepository.save(episode);
                }
            } else {
                Episode episode = Episode.builder()
                        .name("Tập full")
                        .duration(faker.number().numberBetween(90, 120))
                        .displayOrder(1)
                        .videoUrl("https://www.youtube.com/embed/W_0AMP9yO1w?si=JcCeGorHalCHKCPl")
                        .status(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .publishedAt(LocalDateTime.now())
                        .movie(movie)
                        .build();
                episodeRepository.save(episode);
            }
        }
    }


    @Test
    void save_reviews() {
        Faker faker = new Faker();
        Random rd = new Random();

        List<User> users = userRepository.findAll();
        List<Movie> movies = movieRepository.findAll();

        for (Movie movie : movies) {
            // random 10 -> 20 reviews
            for (int i = 0; i < rd.nextInt(11) + 10; i++) {
                User rdUser = users.get(rd.nextInt(users.size()));
                Review review = Review.builder()
                        .content(faker.lorem().paragraph())
                        .rating(rd.nextInt(6) + 5)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .user(rdUser)
                        .movie(movie)
                        .build();
                reviewRepository.save(review);
            }
        }
    }


    @Test
    void testQuery() {
        // Movie movie = movieRepository.findByName("Dinah Soares");
        // System.out.println(movie);

        // Sap xep
//        List<Movie> movieSortByRating = movieRepository
//                .findByRatingLessThan(8.0, Sort.by("rating").descending().and(Sort.by("name").ascending()));
//        movieSortByRating.forEach(movie -> System.out.println(movie.getName() + " - " + movie.getRating()));

        // Phan trang
        Pageable pageable = PageRequest.of(0, 5, Sort.by("rating").descending());
        Page<Movie> moviePage = movieRepository.findByNameContaining("a", pageable);
        System.out.println("Total pages: " + moviePage.getTotalPages());
        System.out.println("Total elements: " + moviePage.getTotalElements());
        moviePage.getContent().forEach(movie -> System.out.println(movie.getName() + " - " + movie.getRating()));
    }

}
