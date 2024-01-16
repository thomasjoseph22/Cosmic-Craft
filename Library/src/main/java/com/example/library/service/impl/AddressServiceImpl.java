package com.example.library.service.impl;

import com.example.library.dto.AddressDto;
import com.example.library.model.Address;
import com.example.library.model.Customer;
import com.example.library.repository.AddressRepository;
import com.example.library.service.AddressService;
import com.example.library.service.CustomerService;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    private CustomerService customerService;
    private AddressRepository addressRepository;


    public AddressServiceImpl(CustomerService customerService, AddressRepository addressRepository) {
        this.customerService = customerService;
        this.addressRepository = addressRepository;
    }
    @Override
    public Address save(AddressDto addressDto, String username) {
        Customer customer=customerService.findByEmail(username);

        Address address=new Address();
        address.setAddress_line_1(addressDto.getAddress_line_1());
        address.setAddress_line_2(addressDto.getAddress_line_2());
        address.setCity(addressDto.getCity());
        address.setCountry(addressDto.getCountry());
        address.setPincode(addressDto.getPincode());
        address.setCustomer(customer);
        address.set_default(false);


        return addressRepository.save(address);
    }

    @Override
    public Address update(AddressDto addressDto) {
        long id=addressDto.getId();
        Address address=addressRepository.findById(id);
        address.setAddress_line_1(addressDto.getAddress_line_1());
        address.setAddress_line_2(addressDto.getAddress_line_2());
        address.setCity(addressDto.getCity());
        address.setCountry(addressDto.getCountry());
        address.setPincode(addressDto.getPincode());
        return addressRepository.save(address);
    }

    @Override
    public AddressDto findById(long id) {
        Address address=addressRepository.findById(id);
        AddressDto addressDto=new AddressDto();
        addressDto.setId(address.getId());
        addressDto.setAddress_line_1(address.getAddress_line_1());
        addressDto.setAddress_line_2(address.getAddress_line_2());
        addressDto.setCity(address.getCity());
        addressDto.setCountry(address.getCountry());
        addressDto.setPincode(address.getPincode());
        addressDto.setCustomer(address.getCustomer());
        return addressDto;
    }

    @Override
    public void deleteAddress(long id) {
        addressRepository.deleteById(id);
    }

    @Override
    public void enable(long id) {
        Address address=addressRepository.findById(id);
        address.set_default(true);

        addressRepository.save(address);
    }

    @Override
    public void disable(long id) {
        Address address = addressRepository.findById(id);
        address.set_default(false);

        addressRepository.save(address);
    }

    @Override
    public Address findDefaultAddress(long customer_id) {
        return addressRepository.findByActivatedTrue(customer_id);
    }

    @Override
    public Address findByIdOrder(long id) {
        return addressRepository.findById(id);
    }
}
