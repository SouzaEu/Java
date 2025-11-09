package com.workbalance.domain.service;

import com.workbalance.api.dto.auth.SignupRequest;
import com.workbalance.api.dto.user.UserResponse;
import com.workbalance.domain.entity.User;
import com.workbalance.infra.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private SignupRequest signupRequest;

    @BeforeEach
    void setUp() {
        signupRequest = new SignupRequest();
        signupRequest.setName("Test User");
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("Test1234");
    }

    @Test
    void signup_ShouldCreateUser_WhenEmailIsUnique() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        
        User savedUser = User.builder()
                .id(UUID.randomUUID())
                .name(signupRequest.getName())
                .email(signupRequest.getEmail())
                .passwordHash("hashedPassword")
                .preferredLanguage("pt-BR")
                .roles(roles)
                .build();
        
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        UserResponse response = authService.signup(signupRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(signupRequest.getName());
        assertThat(response.getEmail()).isEqualTo(signupRequest.getEmail());
        assertThat(response.getRoles()).contains("USER");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void signup_ShouldThrowException_WhenEmailAlreadyExists() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> authService.signup(signupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email already in use");
    }
}
