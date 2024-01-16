package com.example.library.dto;

import com.example.library.model.Customer;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private Long id;
    @NotEmpty(message = "Address Line must be filled")
    private String address_line_1;
    @NotEmpty(message = "Address Line must be filled")
    private String address_line_2;
    @NotEmpty(message = "City must be filled")
    private String city;
    @NotEmpty(message = "Country must be filled")
    private String country;

    @NotEmpty(message = "Pincode must be filled")
    private String pincode;
    private Customer customer;
    private boolean is_default;
}
