package com.example.library.repository;

import com.example.library.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // You can add custom query methods if needed
}