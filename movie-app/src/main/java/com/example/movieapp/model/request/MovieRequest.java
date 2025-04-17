package com.example.movieapp.model.request;

import com.example.movieapp.model.enums.MovieType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class MovieRequest {

    // làm câu 2 tạo phim

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

    private List<Integer> genreIds;   // Có thể rỗng nhưng phải là danh sách số nguyên
    private List<Integer> actorIds;   // Có thể rỗng
    private List<Integer> directorIds; // Có thể rỗng
}
