package com.example.secure_api_lab1.service;

import com.example.secure_api_lab1.dto.AuthRequestDTO;
import com.example.secure_api_lab1.dto.AuthResponseDTO;
import com.example.secure_api_lab1.exception.ErrorResponse;
import com.example.secure_api_lab1.jwt.JwtProvider;
import com.example.secure_api_lab1.model.User;
import com.example.secure_api_lab1.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;


    @Transactional
    public ResponseEntity<?> register(AuthRequestDTO authRequestDTO) {
        Optional<User> optionalUser = userRepository.findUserByUsername(authRequestDTO.getUsername());
        if(optionalUser.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ErrorResponse.builder()
                            .errorCode(HttpStatus.CONFLICT.value())
                            .message(String.format("User with name %s is already exists", authRequestDTO.getUsername()))
                            .build());
        }

        User user = createUser(authRequestDTO);
        User savedUser = userRepository.save(user);

        String accessToken = jwtProvider.generateToken(authRequestDTO.getUsername(), savedUser.getId());

        AuthResponseDTO registerResponseDTO = AuthResponseDTO.builder()
                .token(accessToken)
                .username(authRequestDTO.getUsername())
                .message("The user has successfully sign-up")
                .timestamp(LocalDateTime.now()).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponseDTO);
    }

    private User createUser(AuthRequestDTO authRequestDTO){
        User user = new User();
        user.setUsername(authRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(authRequestDTO.getPassword()));
        return user;
    }

    public ResponseEntity<?> login(AuthRequestDTO authRequestDTO) {

        Optional<User> optionalUser = userRepository.findUserByUsername(authRequestDTO.getUsername());

        if(optionalUser.isEmpty()){
            log.info("User {} is not found", authRequestDTO.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.builder()
                            .errorCode(HttpStatus.UNAUTHORIZED.value())
                            .message("user is not found")
                            .build());

        }
        if(!passwordEncoder.matches(authRequestDTO.getPassword(), optionalUser.get().getPassword())){
            log.info("User password {} isn't correct", authRequestDTO.getPassword());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.builder()
                            .errorCode(HttpStatus.UNAUTHORIZED.value())
                            .message("user is not found")
                            .build());
        }

        String accessToken = jwtProvider.generateToken(authRequestDTO.getUsername(), optionalUser.get().getId());

        AuthResponseDTO authResponseDTO = AuthResponseDTO.builder()
                .token(accessToken)
                .username(authRequestDTO.getUsername())
                .timestamp(LocalDateTime.now())
                .message("The user has successfully logged in")
                .build();

        return ResponseEntity.ok(authResponseDTO);

    }

}
