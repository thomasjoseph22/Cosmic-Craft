package com.example.library.service.impl;

import com.example.library.Exception.CustomerNotFoundException;
import com.example.library.dto.CustomerDto;
import com.example.library.model.Customer;
import com.example.library.repository.CustomerRepository;
import com.example.library.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer findByEmail(String email) {

        return customerRepository.findByEmail(email);
    }

    @Override
    public Customer save(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setMobileNumber(customerDto.getMobileNumber());
        customer.set_activated(true);
        customer.setPassword((customerDto.getPassword()));
        customer.setEmail(customerDto.getEmail());
        customer.setRoles("User");
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer findById(long id) {
        return customerRepository.findById(id);
    }

    @Override
    public void disable(long id) {
        Customer customer=findById(id);
        customer.set_activated(false);
        customerRepository.save(customer);

    }

    @Override
    public void enable(long id) {
        Customer customer = findById(id);
        customer.set_activated(true);
        customerRepository.save(customer);
    }

    @Override
    public Customer update(CustomerDto customerDto) {
        Customer customer=customerRepository.findByEmail(customerDto.getEmail());
        customer.setPassword(customerDto.getPassword());

        return customerRepository.save(customer);
    }

    @Override
    public CustomerDto findByEmailCustomerDto(String email) {
        Customer customer = customerRepository.findByEmail(email);
        CustomerDto customerDto=new CustomerDto();
        customerDto.setEmail(customer.getEmail());
        customerDto.setId(customer.getId());
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setMobileNumber(customer.getMobileNumber());
        customerDto.setAddress(customer.getAddress());
        customerDto.setPassword(customer.getPassword());
        customerDto.set_activated(customer.is_activated());

        return customerDto;
    }

    @Override
    public CustomerDto updateAccount(CustomerDto customerDto, String email) {
        Customer customer= findByEmail(email);
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setMobileNumber(customerDto.getMobileNumber());
        customerRepository.save(customer);
        CustomerDto customerDtoUpdated = convertEntityToDto(customer);
        return customerDtoUpdated;
    }

    @Override
    public void changePass(CustomerDto customerDto) {
        Customer customer=customerRepository.findByEmail(customerDto.getEmail());
        customer.setPassword(customerDto.getPassword());

        customerRepository.save(customer);
    }



    public CustomerDto convertEntityToDto(Customer customer){
        CustomerDto customerDto=new CustomerDto();
        customerDto.setId(customer.getId());
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setMobileNumber(customer.getMobileNumber());
        customerDto.set_activated(customer.is_activated());
        customerDto.setPassword(customer.getPassword());

        return customerDto;
    }






    public void updateResetPasswordToken(String token, String email) throws CustomerNotFoundException {
        Customer customer = customerRepository.findByEmail(email);
        if (customer != null) {
            customer.setResetPasswordToken(token);
            customerRepository.save(customer);
        } else {
            throw new CustomerNotFoundException("Could not find any customer with the email " + email);
        }
    }

    public Customer getByResetPasswordToken(String token) {
        return customerRepository.findByResetPasswordToken(token);
    }

    public void updatePassword(Customer customer, String newPassword) {
        customer.setPassword(newPassword);

        customer.setResetPasswordToken(null);
        customerRepository.save(customer);
    }
}
