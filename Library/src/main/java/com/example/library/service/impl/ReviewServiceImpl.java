package com.example.library.service.impl;

import com.example.library.dto.ReviewDto;
import com.example.library.model.Product;
import com.example.library.model.Review;
import com.example.library.repository.ReviewRepository;
import com.example.library.service.CustomerService;
import com.example.library.service.ProductService;
import com.example.library.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    private ReviewRepository repository;
    private ProductService productService;
    private CustomerService customerService;

    public ReviewServiceImpl(ReviewRepository repository, ProductService productService, CustomerService customerService) {
        this.repository = repository;
        this.productService = productService;
        this.customerService = customerService;
    }

    @Override
    public Review save(ReviewDto reviewDto, String email, Long productId) {
        Product product=productService.getById(productId);
        Review review=new Review();
        review.setComment(reviewDto.getComment());
        review.setReviewDate(new Date());
        review.setRating(reviewDto.getRating());
        review.setCustomer(customerService.findByEmail(email));
        review.setProduct(product);
        return repository.save(review);
    }

    @Override
    public List<Review> readReviewByProduct(Long productId) {

        return repository.findByProduct_Id(productId);
    }

    @Override
    public Double findRatingByProduct(Long productId) {

        Object average=repository.findAverageRatingByProduct(productId);
        Double avgrating= (Double) average;
        return avgrating;
    }
}