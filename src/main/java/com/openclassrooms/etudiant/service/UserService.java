package com.openclassrooms.etudiant.service;

import com.openclassrooms.etudiant.dto.RegisterDTO;
import com.openclassrooms.etudiant.entities.User;
import com.openclassrooms.etudiant.mapper.UserDtoMapper;
import com.openclassrooms.etudiant.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;    
    private final JwtService jwtService;
    private final UserDtoMapper userDtoMapper;

    public void register(RegisterDTO registerDto) {
        User user = userDtoMapper.toEntity(registerDto);
        
        Assert.notNull(user, "User must not be null");
        log.info("Registering new user");

        Optional<User> optionalUser = userRepository.findByLogin(user.getLogin());
        if (optionalUser.isPresent()) {
            throw new IllegalArgumentException("User with login " + user.getLogin() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public String login(String login, String password) {
        Assert.notNull(login, "Login must not be null");
        Assert.notNull(password, "Password must not be null");
        Optional<User> user = userRepository.findByLogin(login);
        User finalUser = user.orElseThrow(() -> new IllegalArgumentException("User with login " + login + " not found"));
        String userPassword = finalUser.getPassword();
        if (passwordEncoder.matches(password, userPassword)) {
            return jwtService.generateToken(finalUser);
        } else {
            throw new IllegalArgumentException("Invalid credentials");
        }
    }


}
