package com.example.movieapp.api;

import com.example.movieapp.entity.Review;
import com.example.movieapp.model.request.CreateReviewRequest;
import com.example.movieapp.model.request.UpdateReviewRequest;
import com.example.movieapp.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewApi {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<?> getReviews(@RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "5") Integer pageSize,
                                        @RequestParam Integer movieId) {
        Page<Review> reviewPage = reviewService.getReviewsByMovie(movieId, page, pageSize);
        return ResponseEntity.ok(reviewPage);
    }

    @PostMapping
    public ResponseEntity<?> createReview(@Valid @RequestBody CreateReviewRequest request, HttpSession session) {
        Review review = reviewService.createReview(request, session);
        return ResponseEntity.ok(review);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(@PathVariable Integer id,
                                          @Valid @RequestBody UpdateReviewRequest request,
                                          HttpSession session) {
        Review review = reviewService.updateReview(id, request, session);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Integer id, HttpSession session) {
        reviewService.deleteReview(id, session);
        return ResponseEntity.ok().build();
    }
}
