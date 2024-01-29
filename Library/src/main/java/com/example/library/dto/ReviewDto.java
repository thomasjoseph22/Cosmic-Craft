package com.example.library.dto;

import com.example.library.model.Customer;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewDto {
    private Long id;
    private String comment;
    private LocalDate reviewDate;
    private Customer customer;
    private Double rating;
    private Double avgRating;
}


