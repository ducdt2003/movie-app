package com.example.movieapp.api;

import com.example.movieapp.entity.Episode;
import com.example.movieapp.service.EpisodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/episodes")
public class AdminEpisodeApi {

    @Autowired
    private EpisodeService episodeService;



    // 📌 Lấy danh sách tập phim theo movieId + caau 1 tập phim
    @GetMapping("/{movieId}")
    public List<Episode> getEpisodesByMovieId(@PathVariable Integer movieId) {
        return episodeService.findEpisodesByMovieId(movieId);
    }

    // 📌 Lấy một tập phim theo movieId & displayOrder + câu 1 tập phim
    @GetMapping("/{movieId}/episode")
    public Episode getEpisodeByDisplayOrder(@PathVariable Integer movieId,
                                            @RequestParam String tap) {
        return episodeService.findEpisodeByDisplayOrder(movieId, tap);
    }
}
