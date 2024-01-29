package com.example.library.service;



import com.example.library.dto.ReviewDto;
import com.example.library.model.Review;

import java.util.List;

public interface ReviewService {
    Review save(ReviewDto reviewDto, String email, Long productId);

    List<Review> readReviewByProduct(Long productId);

    Double findRatingByProduct(Long productId);
}