package com.example.demoPractiseSecurity.service;

import com.example.demoPractiseSecurity.jwt.JwtUtil;
import com.example.demoPractiseSecurity.models.Userrs;
import com.example.demoPractiseSecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.BadCredentialsException;


@Service
public class UserService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtil jwtUtil;

    public void signup(String username, String password, String role) {
        if (userRepo.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        Userrs user = new Userrs();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        userRepo.save(user);
    }

    public String login(String username, String password) {
        Userrs user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid user"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return jwtUtil.generateToken(user.getUsername(), user.getRole());
    }

}
