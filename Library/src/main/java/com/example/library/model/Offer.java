package com.example.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "offers")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "offer_id")
    private Long id;

    private String name;

    private String description;

    private int offPercentage;

    private String offerType;

    @Column(nullable = true)
    private Long offerProductId;
    @Column(nullable = true)
    private String applicableForProductName;
    @Column(nullable = true)
    private Long offerCategoryId;
    @Column(nullable = true)
    private String applicableForCategoryName;

    private boolean enabled;
}
