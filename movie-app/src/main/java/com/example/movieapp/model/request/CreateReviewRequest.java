package com.example.movieapp.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequest {
    @NotNull(message = "Nội dung không được để trống")
    private String content;

    @NotNull(message = "Rating không được để trống")
    @Min(value = 1, message = "Rating phải lớn hơn hoặc bằng 1")
    @Max(value = 10, message = "Rating phải nhỏ hơn hoặc bằng 10")
    private Integer rating;

    @NotNull(message = "movieId không được để trống")
    private Integer movieId;
}
