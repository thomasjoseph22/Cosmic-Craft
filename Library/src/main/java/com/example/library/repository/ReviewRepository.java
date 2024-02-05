package com.example.library.repository;

import com.example.library.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {


    @Query("select r from Review r where r.product.id=?1")
    List<Review> readReviewByProduct(Long productId);

    @Query("SELECT AVG (r.rating) as avgRating from Review r where r.product.id=?1")
    Object findRatingByProduct(Long productId);
}
