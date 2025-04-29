package com.example.movieapp.model.request;

import com.example.movieapp.model.enums.MovieType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class MovieRequest {
    private Integer id; // Thêm id để sử dụng trong th:action

    @NotEmpty(message = "Tên phim không được để trống")
    private String name;

    @NotEmpty(message = "Trailer URL không được để trống")
    private String trailerUrl;

    @NotEmpty(message = "Mô tả phim không được để trống")
    private String description;

    @NotNull(message = "Năm phát hành không được null")
    private Integer releaseYear;

    @NotNull(message = "Loại phim không được null")
    private MovieType type;

    @NotNull(message = "Trạng thái phim không được null")
    private Boolean status;

    @NotNull(message = "Mã quốc gia không được null")
    private Integer countryId;

    private List<Integer> genreIds;
    private List<Integer> actorIds;
    private List<Integer> directorIds;

    @Override
    public String toString() {
        return "MovieRequest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", trailerUrl='" + trailerUrl + '\'' +
                ", description='" + description + '\'' +
                ", releaseYear=" + releaseYear +
                ", type=" + type +
                ", status=" + status +
                ", countryId=" + countryId +
                ", genreIds=" + genreIds +
                ", actorIds=" + actorIds +
                ", directorIds=" + directorIds +
                '}';
    }
}