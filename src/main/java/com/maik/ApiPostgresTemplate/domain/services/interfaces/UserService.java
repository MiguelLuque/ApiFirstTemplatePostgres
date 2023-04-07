package com.maik.ApiPostgresTemplate.domain.services.interfaces;

import com.maik.ApiPostgresTemplate.domain.entities.Pet;
import com.maik.ApiPostgresTemplate.domain.entities.User;
import com.maik.ApiPostgresTemplate.models.dto.AuthRequest;
import com.maik.ApiPostgresTemplate.models.dto.AuthResponse;
import com.maik.ApiPostgresTemplate.models.dto.PetDTO;
import com.maik.ApiPostgresTemplate.models.dto.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> findAll();

    UserDTO findById(Long id);

    UserDTO findByEmail(String email);

    User findDbUserByEmail(String email);

    UserDTO save(User user);

    void delete(Long id);
}
