package com.appointment_scheduling_app.security.service;

import com.appointment_scheduling_app.security.dto.*;
import com.appointment_scheduling_app.security.entity.OurUser;
import com.appointment_scheduling_app.security.exception.UserAlreadyExistsWithEmailException;
import com.appointment_scheduling_app.security.repository.OurUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Optional;

@Service
public class UserManagementService {
    @Autowired
    private OurUserRepository ourUserRepository;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean deleteByUserId(long userId) {
        ourUserRepository.deleteByUserId(userId);
        return true;
    }

    public boolean isUserDatabaseEmpty() {
        return ourUserRepository.count() == 0;
    }

    public void createAdminUserIfNotExists(String email, String password, String role) {
        if (ourUserRepository.findByEmail(email).isEmpty()) {
            OurUser user = new OurUser();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(role);
            ourUserRepository.save(user);
        }
    }

    @Transactional
    public boolean register(RegisterRequestDTO registerRequestDTO){
        RegisterResponseDTO registerResponseDTO = new RegisterResponseDTO();
        OurUser ourUser = new OurUser();
        Optional<OurUser> user = ourUserRepository.findByEmail(registerRequestDTO.getEmail());
        if (user.isPresent()){
            throw new UserAlreadyExistsWithEmailException("User already exists with email " + registerRequestDTO.getEmail());
        }
        else {
            ourUser.setUserId(registerRequestDTO.getUserId());
            ourUser.setName(registerRequestDTO.getName());
            ourUser.setEmail(registerRequestDTO.getEmail());
            ourUser.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
            ourUser.setRole(registerRequestDTO.getRole());
            ourUserRepository.save(ourUser);
        }
        return ourUser.getId() > 0;
    }

    @Transactional
    public boolean registerAdmin(RegisterRequestDTO registerRequestDTO){
        RegisterResponseDTO registerResponseDTO = new RegisterResponseDTO();
        OurUser ourUser = new OurUser();
        Optional<OurUser> user = ourUserRepository.findByEmail(registerRequestDTO.getEmail());
        if (user.isPresent()){
            throw new UserAlreadyExistsWithEmailException("User already exists with email " + registerRequestDTO.getEmail());
        }
        else {
            ourUser.setUserId(registerRequestDTO.getUserId());
            ourUser.setName(registerRequestDTO.getName());
            ourUser.setEmail(registerRequestDTO.getEmail());
            ourUser.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
            ourUser.setRole("ADMIN");
            ourUserRepository.save(ourUser);
        }
        return ourUser.getId() > 0;
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
            );
            var user = ourUserRepository.findByEmail(loginRequestDTO.getEmail()).orElseThrow();
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

            loginResponseDTO.setStatusCode(200);
            loginResponseDTO.setToken(jwt);
            loginResponseDTO.setRefreshToken(refreshToken);
            loginResponseDTO.setExpirationTime("1 hour");
            loginResponseDTO.setMessage("Successfully logged in");
        } catch (Exception e) {
            loginResponseDTO.setStatusCode(401);
            loginResponseDTO.setMessage("Authentication failed");
        }
        return loginResponseDTO;
    }

    public LoginResponseDTO refreshToken(LoginResponseDTO refreshTokenRequest) {
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        try {
            String email = jwtUtils.extractUsername(refreshTokenRequest.getToken());
            OurUser user = ourUserRepository.findByEmail(email).orElseThrow();
            if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), user)) {
                var jwt = jwtUtils.generateToken(user);
                loginResponseDTO.setStatusCode(200);
                loginResponseDTO.setToken(jwt);
                loginResponseDTO.setRefreshToken(refreshTokenRequest.getToken());
                loginResponseDTO.setExpirationTime("1Hr");
                loginResponseDTO.setMessage("Successfully Refreshed Token");
            } else {
                loginResponseDTO.setStatusCode(401);
                loginResponseDTO.setMessage("Invalid refresh token");
            }
        } catch (Exception e) {
            loginResponseDTO.setStatusCode(500);
            loginResponseDTO.setMessage("Failed to refresh token: " + e.getMessage());
        }
        return loginResponseDTO;
    }
}
