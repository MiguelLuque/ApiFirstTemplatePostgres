package com.maik.ApiPostgresTemplate.domain.services.interfaces;

import com.maik.ApiPostgresTemplate.domain.entities.Role;
import com.maik.ApiPostgresTemplate.models.dto.AuthRequest;
import com.maik.ApiPostgresTemplate.models.dto.AuthResponse;

public interface AuthService {

    AuthResponse login(AuthRequest authRequest);

    AuthResponse registration(AuthRequest authRequest);

    AuthResponse refreshToken(String refreshToken);

    Role findRoleByName(String name);
}
