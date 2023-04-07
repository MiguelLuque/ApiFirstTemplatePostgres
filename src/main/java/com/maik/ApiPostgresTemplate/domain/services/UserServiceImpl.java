package com.maik.ApiPostgresTemplate.domain.services;

import com.maik.ApiPostgresTemplate.config.JwtTokenUtil;
import com.maik.ApiPostgresTemplate.domain.entities.User;
import com.maik.ApiPostgresTemplate.domain.mappers.UserMapper;
import com.maik.ApiPostgresTemplate.domain.repositories.UserRepository;
import com.maik.ApiPostgresTemplate.domain.services.interfaces.AuthService;
import com.maik.ApiPostgresTemplate.domain.services.interfaces.UserService;
import com.maik.ApiPostgresTemplate.models.dto.AuthRequest;
import com.maik.ApiPostgresTemplate.models.dto.AuthResponse;
import com.maik.ApiPostgresTemplate.models.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public List<UserDTO> findAll() {
        return null;
    }

    @Override
    public UserDTO findById(Long id) {
        return userRepository.findById(id).map(userMapper::toDto).orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    @Override
    public UserDTO findByEmail(String email) {

        return userMapper.toDto(findDbUserByEmail(email));
    }

    @Override
    public User findDbUserByEmail(String email) {

        return userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    @Override
    public UserDTO save(User user) {
        if(checkIfUserExists(user.getEmail())) {
            throw new IllegalArgumentException("User already exists");
        }
        User newUser = userRepository.save(user);
        return userMapper.toDto(newUser);
    }

    @Override
    public void delete(Long id) {

    }

    private boolean checkIfUserExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }


}
