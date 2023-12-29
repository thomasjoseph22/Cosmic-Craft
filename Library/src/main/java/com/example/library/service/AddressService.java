package com.example.library.service;

import com.example.library.dto.AddressDto;
import com.example.library.model.Address;

public interface AddressService {
    Address save(AddressDto addressDto, String username);

    Address update(AddressDto addressDto);

    AddressDto findById(long id);

    void deleteAddress(long id);

    void enable(long id);

    void disable(long id);

    Address findDefaultAddress(long customer_id);

    Address findByIdOrder(long id);
}
