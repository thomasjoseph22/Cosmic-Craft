package com.example.admin.config;

import com.example.library.model.Admin;
import com.example.library.model.Role;
import com.example.library.repository.AdminRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminDetailsService implements UserDetailsService {

    private AdminRepository adminRepository;

    public AdminDetailsService(AdminRepository adminRepository) {

        this.adminRepository = adminRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByEmail(email);

        if (admin != null) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            for(Role role : admin.getRoles()){
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            }

            return new AdminDetails(
                    admin.getEmail(),
                    admin.getPassword(),
                    authorities,
                    admin.getFirstName(),
                    admin.getLastName(),
                    admin.getMobileNumber()
            );

        }else {
            throw new UsernameNotFoundException("Invalid username or password.");
        }

    }
}
