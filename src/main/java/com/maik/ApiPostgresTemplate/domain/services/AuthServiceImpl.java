package com.maik.ApiPostgresTemplate.domain.services;

import com.maik.ApiPostgresTemplate.config.JwtTokenUtil;
import com.maik.ApiPostgresTemplate.domain.entities.Role;
import com.maik.ApiPostgresTemplate.domain.entities.User;
import com.maik.ApiPostgresTemplate.domain.mappers.UserMapper;
import com.maik.ApiPostgresTemplate.domain.repositories.RoleRepository;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final RoleRepository roleRepository;
    private final UserService userService;



    @Override
    public AuthResponse login(AuthRequest authRequest) {
        AuthResponse response = new AuthResponse();
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(), authRequest.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String accessToken = jwtTokenUtil.generateAccessToken(user);
        String refreshToken = jwtTokenUtil.generateRefreshToken(user);

        response.setEmail(user.getEmail());
        response.setToken(accessToken);
        response.setRefreshToken(refreshToken);
        return response;
    }

    @Override
    public AuthResponse registration(AuthRequest authRequest) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode(authRequest.getPassword());
        Role customerRole = this.findRoleByName("ROLE_CUSTOMER");
        //Create user with authRequest
        User user = User
                .builder()
                .email(authRequest.getEmail())
                .password(password)
                .roles(new HashSet<>(List.of(customerRole)))
                .build();

        //Save user to database
        UserDTO userDTO = userService.save(user);

        return this.login(authRequest);
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        AuthResponse response = new AuthResponse();
        String subject = jwtTokenUtil.getSubject(refreshToken);
        User user = userService.findDbUserByEmail(subject.split(",")[1]);
        String newAccessToken = jwtTokenUtil.generateAccessToken(user);

        response.setToken(newAccessToken);
        return response;
    }

    @Override
    public Role findRoleByName(String name) {
        return roleRepository.findByName(name).orElseThrow(() -> new NoSuchElementException("Role not found"));
    }


}
