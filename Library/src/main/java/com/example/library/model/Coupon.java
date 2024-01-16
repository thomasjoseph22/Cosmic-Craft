package com.example.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    private String code;

    private String description;

    private int count;

    private int offPercentage;

    private int maxOff;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;


    private boolean enabled;

    public boolean isExpired(){
        return (this.count == 0 || this.expiryDate.isBefore(LocalDate.now()));
    }


    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", count=" + count +
                ", offPercentage=" + offPercentage +
                ", maxOff=" + maxOff +
                ", expiryDate=" + expiryDate +
                ", enabled=" + enabled +
                '}';
    }
}
