package com.enrique.reservatusalaback.security;

import com.enrique.reservatusalaback.model.Business;
import com.enrique.reservatusalaback.repository.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private BusinessRepository businessRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Business business = businessRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Business with email " + email + "not exists."));

        return new UserDetailsImpl(business);
    }
}
