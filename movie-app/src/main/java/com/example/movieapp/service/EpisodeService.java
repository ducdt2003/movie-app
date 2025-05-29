package com.example.movieapp.service;

import com.example.movieapp.entity.Episode;
import com.example.movieapp.entity.Movie;
import com.example.movieapp.exception.BadRequestException;
import com.example.movieapp.exception.NotFoundException;
import com.example.movieapp.model.request.EpisodeRequest;
import com.example.movieapp.repository.EpisodeRepository;
import com.example.movieapp.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EpisodeService {
    private final EpisodeRepository episodeRepository;
    private final MovieRepository movieRepository;
    private final CloudinaryService cloudinaryService;

    // câu 1 lấy tập phim

    public List<Episode> findEpisodesByMovieId(Integer id) {
        return episodeRepository.findByMovie_IdAndStatusOrderByDisplayOrderAsc(id, true);
    }



    public Episode findEpisodeByDisplayOrder(Integer id, String tap) {
        Integer displayOrder = tap.equals("full") ? 1 : Integer.parseInt(tap);
        // select * from episodes where movie_id = id and display_order = displayOrder and status = true
        return episodeRepository.findByMovie_IdAndDisplayOrderAndStatus(id, displayOrder, true);
    }


    // câu 2 tạo mới tập phim

    public Episode createEpisode(EpisodeRequest episodeRequest) {
        Movie movie = movieRepository.findById(episodeRequest.getMovieId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phim với ID: " + episodeRequest.getMovieId()));

        Episode episode = Episode.builder()
                .name(episodeRequest.getName())
                .duration(episodeRequest.getDuration())
                .displayOrder(episodeRequest.getDisplayOrder())
                .videoUrl(episodeRequest.getVideoUrl())
                .status(episodeRequest.getStatus())
                .movie(movie)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .publishedAt(LocalDateTime.now()) // Nếu cần xuất bản ngay
                .build();

        return episodeRepository.save(episode);
    }

    // cập nhaath tập phim cây 3
    public Episode updateEpisode(Integer id, EpisodeRequest episodeRequest) {
        Episode episode = episodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tập phim với ID: " + id));

        episode.setName(episodeRequest.getName());
        episode.setDisplayOrder(episodeRequest.getDisplayOrder());
        episode.setStatus(episodeRequest.getStatus());
        episode.setUpdatedAt(LocalDateTime.now());

        return episodeRepository.save(episode);
    }


    // xóa tập phim
    @Transactional
    public void deleteEpisode(Integer id) {
        Episode episode = episodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tập phim với ID: " + id));

        episodeRepository.delete(episode);
    }


    public Map uploadVideo(Integer id, MultipartFile file) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phim với id " + id));
        try {
            Map map = cloudinaryService.uploadFile(file, "file_video_27_28");
            String url = map.get("url").toString();
            movie.setTrailer(url); // Assuming Movie class has setVideo method
            movie.setUpdatedAt(LocalDateTime.now());
            movieRepository.save(movie);
            return Map.of("url", url);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }


}
