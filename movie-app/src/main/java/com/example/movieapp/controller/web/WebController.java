package com.example.movieapp.controller.web;

import com.example.movieapp.entity.Episode;
import com.example.movieapp.entity.Movie;
import com.example.movieapp.model.enums.MovieType;
import com.example.movieapp.service.EpisodeService;
import com.example.movieapp.service.FavoriteService;
import com.example.movieapp.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebController {
    private final MovieService movieService;
    private final EpisodeService episodeService;
    private final FavoriteService favoriteService;

    @GetMapping("/")
    public String getHomePage(Model model) {
        List<Movie> hotMovies = movieService.findHotMovie(true, 4);
        List<Movie> phimLeList = movieService.findByType(MovieType.PHIM_LE, true, 1, 6).getContent();
        List<Movie> phimBoList = movieService.findByType(MovieType.PHIM_BO, true, 1, 6).getContent();
        List<Movie> phimChieuRapList = movieService.findByType(MovieType.PHIM_CHIEU_RAP, true, 1, 6).getContent();
        model.addAttribute("hotMovies", hotMovies);
        model.addAttribute("phimLeList", phimLeList);
        model.addAttribute("phimBoList", phimBoList);
        model.addAttribute("phimChieuRapList", phimChieuRapList);
        return "web/index";
    }

    @GetMapping("/phim-bo")
    public String getPhimBoPage(@RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "18") Integer pageSize,
                                Model model) {
        Page<Movie> moviePage = movieService.findByType(MovieType.PHIM_BO, true, page, pageSize);
        model.addAttribute("moviePage", moviePage);
        model.addAttribute("currentPage", page);
        return "web/phim-bo";
    }

    @GetMapping("/phim-le")
    public String getPhimLePage(@RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "18") Integer pageSize,
                                Model model) {
        Page<Movie> moviePage = movieService.findByType(MovieType.PHIM_LE, true, page, pageSize);
        model.addAttribute("moviePage", moviePage);
        model.addAttribute("currentPage", page);
        return "web/phim-le";
    }

    @GetMapping("/phim-chieu-rap")
    public String getPhimChieuRapPage(@RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "18") Integer pageSize,
                                Model model) {
        Page<Movie> moviePage = movieService.findByType(MovieType.PHIM_CHIEU_RAP, true, page, pageSize);
        model.addAttribute("moviePage", moviePage);
        model.addAttribute("currentPage", page);
        return "web/phim-chieu-rap";
    }

    /*@GetMapping("/phim/{id}/{slug}")
    public String getMovieDetailsPage(@PathVariable Integer id, @PathVariable String slug, Model model) {
        // Thông tin chi tiết phim
        Movie movie = movieService.findMovieDetails(id, slug);
        model.addAttribute("movie", movie);

        // Lấy danh sách tập phim (movieId, status = true, sort by displayOrder asc)
        List<Episode> episodes = episodeService.findEpisodesByMovieId(id);
        model.addAttribute("episodes", episodes);
        return "web/chi-tiet-phim";
    }
*/

    @GetMapping("/phim/{id}/{slug}")
    public String getMovieDetailsPage(@PathVariable Integer id, @PathVariable String slug, Model model) {
        Movie movie = movieService.findMovieDetails(id, slug);
        model.addAttribute("movie", movie);

        List<Episode> episodes = episodeService.findEpisodesByMovieId(id);
        model.addAttribute("episodes", episodes);

        // 🔹 Lấy danh sách phim liên quan
        List<Movie> relatedMovies = movieService.findRelatedMovies(id);
        model.addAttribute("relatedMovies", relatedMovies);

        return "web/chi-tiet-phim";
    }



    // /xem-phim/{id}/{slug}?tap=1, 2, 3 -> Phim bo
    // /xem-phim/{id}/{slug}?tap=full -> Phim le, phim chieu rap
    @GetMapping("/xem-phim/{id}/{slug}")
    public String getWatchMovieDetailsPage(@PathVariable Integer id, @PathVariable String slug, Model model, @RequestParam String tap) {
        // Thông tin chi tiết phim
        Movie movie = movieService.findMovieDetails(id, slug);
        model.addAttribute("movie", movie);

        // Lấy danh sách tập phim (movieId, status = true, sort by displayOrder asc)
        List<Episode> episodes = episodeService.findEpisodesByMovieId(id);
        model.addAttribute("episodes", episodes);

        // Lấy thông tin tập phim (tap = displayOrder)
        // tap = "1" => displayOrder = 1
        // tap = "2" => displayOrder = 2
        // tap = "full" => displayOrder = 1
        // Tìm kiếm (movieId, status = true, displayOrder = tap)
        // Lấy tập phim theo `tap`
        Episode episode = episodeService.findEpisodeByDisplayOrder(id, tap);
        model.addAttribute("episode", episode);

        // 🔹 Lấy danh sách phim liên quan
        List<Movie> relatedMovies = movieService.findRelatedMovies(id);
        model.addAttribute("relatedMovies", relatedMovies);

        return "web/xem-phim";
    }

    /*@GetMapping("/phim-yeu-thich")
    public String getFavoritePage(Model model){
        return "web/favorites";
    }*/

    @GetMapping("/phim-yeu-thich")
    public String getFavoritePage(@RequestParam Integer userId,
                                  @RequestParam(defaultValue = "1") Integer page,
                                  Model model) {
        Page<Movie> moviePage = favoriteService.getUserFavoriteMovies(userId, page, 4);
        model.addAttribute("userId", userId); // Truyền userId vào model
        model.addAttribute("moviePage", moviePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", moviePage.getTotalPages());
        return "web/favorites";
    }

    @GetMapping("/dang-nhap")
    public String showLoginPage() {
            return "web/login";
    }
}
