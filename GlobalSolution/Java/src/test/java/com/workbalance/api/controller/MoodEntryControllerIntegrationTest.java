package com.workbalance.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workbalance.api.dto.mood.CreateMoodEntryRequest;
import com.workbalance.domain.entity.User;
import com.workbalance.infra.repository.MoodEntryRepository;
import com.workbalance.infra.repository.UserRepository;
import com.workbalance.infra.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration test for MoodEntryController
 * Tests the full stack: Controller -> Service -> Repository -> Database
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class MoodEntryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MoodEntryRepository moodEntryRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String jwtToken;
    private User testUser;

    @BeforeEach
    void setUp() {
        moodEntryRepository.deleteAll();
        userRepository.deleteAll();

        // Create test user
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        
        testUser = User.builder()
                .name("Test User")
                .email("test@example.com")
                .passwordHash(passwordEncoder.encode("password123"))
                .preferredLanguage("pt-BR")
                .roles(roles)
                .build();
        testUser = userRepository.save(testUser);

        // Generate JWT token
        jwtToken = jwtTokenProvider.generateToken(testUser.getId(), testUser.getEmail(), testUser.getRoles());
    }

    @Test
    void shouldCreateMoodEntrySuccessfully() throws Exception {
        CreateMoodEntryRequest request = new CreateMoodEntryRequest(
                LocalDate.now(),
                4,
                3,
                5,
                "Feeling productive today",
                List.of("focused", "energetic")
        );

        mockMvc.perform(post("/api/v1/mood-entries")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mood").value(4))
                .andExpect(jsonPath("$.stress").value(3))
                .andExpect(jsonPath("$.productivity").value(5))
                .andExpect(jsonPath("$.notes").value("Feeling productive today"));
    }

    @Test
    void shouldRejectDuplicateMoodEntry() throws Exception {
        CreateMoodEntryRequest request = new CreateMoodEntryRequest(
                LocalDate.now(),
                4,
                3,
                5,
                "First entry",
                null
        );

        // Create first entry
        mockMvc.perform(post("/api/v1/mood-entries")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Try to create duplicate
        mockMvc.perform(post("/api/v1/mood-entries")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("MOOD_ENTRY_ALREADY_EXISTS"));
    }

    @Test
    void shouldRejectInvalidMoodValues() throws Exception {
        CreateMoodEntryRequest request = new CreateMoodEntryRequest(
                LocalDate.now(),
                6, // Invalid: must be 1-5
                3,
                5,
                null,
                null
        );

        mockMvc.perform(post("/api/v1/mood-entries")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"));
    }

    @Test
    void shouldRejectDateOlderThan30Days() throws Exception {
        CreateMoodEntryRequest request = new CreateMoodEntryRequest(
                LocalDate.now().minusDays(31), // Too old
                4,
                3,
                5,
                null,
                null
        );

        mockMvc.perform(post("/api/v1/mood-entries")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"));
    }

    @Test
    void shouldGetMoodEntriesWithPagination() throws Exception {
        // Create multiple entries
        for (int i = 0; i < 3; i++) {
            CreateMoodEntryRequest request = new CreateMoodEntryRequest(
                    LocalDate.now().minusDays(i),
                    4,
                    3,
                    5,
                    "Day " + i,
                    null
            );

            mockMvc.perform(post("/api/v1/mood-entries")
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        // Get entries with pagination
        mockMvc.perform(get("/api/v1/mood-entries")
                        .header("Authorization", "Bearer " + jwtToken)
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(2))
                .andExpect(jsonPath("$.total").value(3));
    }

    @Test
    void shouldRequireAuthentication() throws Exception {
        CreateMoodEntryRequest request = new CreateMoodEntryRequest(
                LocalDate.now(),
                4,
                3,
                5,
                null,
                null
        );

        mockMvc.perform(post("/api/v1/mood-entries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}
