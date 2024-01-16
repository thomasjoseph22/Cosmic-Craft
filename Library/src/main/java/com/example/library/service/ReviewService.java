package com.example.library.service;

import com.example.library.dto.ReviewDto;
import com.example.library.model.Review;
import com.example.library.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review saveReview(ReviewDto reviewDto) {
        // Convert ReviewDto to Review entity and save
        Review review = new Review();
        review.setComment(reviewDto.getComment());
        // Set other fields as needed
        return reviewRepository.save(review);
    }
}
