package com.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponDto {
    private Long id;

    private String code;

    private String description;

    private int count;

    private int offPercentage;

    private int maxOff;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;


    private boolean enabled;
}
