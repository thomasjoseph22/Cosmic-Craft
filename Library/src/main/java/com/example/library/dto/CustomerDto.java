package com.example.library.dto;

import com.example.library.model.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto implements Serializable {
    private long id;
    @NotNull(message="name not null")
    private String firstName;
    @NotNull(message="name not null")
    private String lastName;
    @NotNull(message = "please enter email")
    @Email
    private String email;
    @NotNull(message = "enter mobile")
    private  String mobileNumber;
    @NotNull(message = "password not null")
    @Size(min = 3,message = "minimum 3 letter required")
    private String password;
    private String confirmPassword;
    private boolean is_activated;
    private List<Address> address;
    private String role;
}
