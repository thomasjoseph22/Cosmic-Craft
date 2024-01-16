package com.example.library.model;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@EqualsAndHashCode
@Table(name = "customers", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Customer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String mobileNumber;

    private String roles;

    private static final long OTP_VALID_DURATION = 5 * 60 * 1000;
    @Column(name = "one_time_password")
    private String otp;

    @Column(name = "otp_requested_time")
    private LocalDateTime otpRequestedTime;
    @Column(name = "is_activated")
    private boolean is_activated;

    @OneToOne(mappedBy = "customer")
    private Wallet wallet;

    @OneToMany(mappedBy = "customer")
    private List<Wishlist> wishlists;


    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private ShoppingCart cart;


    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Address> address;



    @Column(name = "reset_password_token")
    private String resetPasswordToken;





}
