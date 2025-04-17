package com.example.movieapp.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EpisodeRequest {
    @NotBlank(message = "Tên tập phim không được để trống")
    private String name;

    @NotNull(message = "Thời lượng tập phim là bắt buộc")
    private Integer duration;

    @NotNull(message = "Thứ tự hiển thị là bắt buộc")
    private Integer displayOrder;

    @NotBlank(message = "URL video không được để trống")
    private String videoUrl;

    @NotNull(message = "Trạng thái là bắt buộc")
    private Boolean status;

    @NotNull(message = "Movie ID là bắt buộc")
    private Integer movieId;
}

