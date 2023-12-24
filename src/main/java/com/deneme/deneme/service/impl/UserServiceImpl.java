package com.deneme.deneme.service.impl;

import com.deneme.deneme.reposityor.UserRepo;
import com.deneme.deneme.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo reposityor;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return reposityor.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

}
