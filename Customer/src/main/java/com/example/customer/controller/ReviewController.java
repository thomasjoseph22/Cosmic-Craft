package com.example.customer.controller;

import com.example.library.dto.ReviewDto;
import com.example.library.model.Review;
import com.example.library.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @RestController
    @RequestMapping("/customer")
    public class YourController {

        @PostMapping("/add-review/{productId}")
        public ResponseEntity<String> addReview(@PathVariable Long productId, @RequestBody ReviewDto reviewDto) {
            // Validate and save the review (you need to create the ReviewDto class)
            Review savedReview = reviewService.saveReview(reviewDto);

            if (savedReview != null) {
                return new ResponseEntity<>("Review added successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Error adding review", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
}

