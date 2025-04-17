package com.example.movieapp.api;

import com.example.movieapp.entity.Episode;
import com.example.movieapp.model.request.EpisodeRequest;
import com.example.movieapp.service.EpisodeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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


    // thêm mới tập phim - câu 2
    @PostMapping
    public ResponseEntity<Episode> createEpisode(@Valid @RequestBody EpisodeRequest episodeRequest) {
        Episode newEpisode = episodeService.createEpisode(episodeRequest);
        return ResponseEntity.ok(newEpisode);
    }




    // cập nhật tâ phim - câu 3
    @PutMapping("/{id}")
    public ResponseEntity<Episode> updateEpisode(@PathVariable Integer id,
                                                 @Valid @RequestBody EpisodeRequest episodeRequest) {
        Episode updatedEpisode = episodeService.updateEpisode(id, episodeRequest);
        return ResponseEntity.ok(updatedEpisode);
    }

    // xóa tập phim - câu 4
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEpisode(@PathVariable Integer id) {
        episodeService.deleteEpisode(id);
        return ResponseEntity.ok("Xóa tập phim thành công với ID: " + id);
    }



}
