package com.typemaster.service;

import com.typemaster.dto.AuthResponse;
import com.typemaster.dto.LoginRequest;
import com.typemaster.dto.RegisterRequest;
import com.typemaster.model.User;
import com.typemaster.repository.UserRepository;
import com.typemaster.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service providing user registration, BCrypt password hashing, and JWT authentication logic.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Registers a new user. Hashes password using BCrypt and generates a JWT token.
     */
    public AuthResponse registerUser(RegisterRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            return new AuthResponse(false, "Username is required.");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            return new AuthResponse(false, "Email is required.");
        }
        if (request.getPassword() == null || request.getPassword().length() < 4) {
            return new AuthResponse(false, "Password must be at least 4 characters long.");
        }

        String username = request.getUsername().trim();
        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByUsername(username)) {
            return new AuthResponse(false, "Username '" + username + "' is already taken.");
        }

        if (userRepository.existsByEmail(email)) {
            return new AuthResponse(false, "An account with email '" + email + "' already exists.");
        }

        // Hash password with BCrypt before persisting to MySQL DB
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(username, email, hashedPassword);
        User savedUser = userRepository.save(user);

        // Generate signed JWT token
        String jwtToken = jwtTokenProvider.generateToken(savedUser.getUsername(), savedUser.getEmail(), savedUser.getId());

        return new AuthResponse(true, "Registration successful!", savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(), jwtToken);
    }

    /**
     * Authenticates an existing user by username/email and BCrypt password hash.
     * Auto-upgrades legacy plain-text passwords to BCrypt hashes in MySQL on successful login.
     */
    public AuthResponse loginUser(LoginRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            return new AuthResponse(false, "Username or email is required.");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            return new AuthResponse(false, "Password is required.");
        }

        String identifier = request.getUsername().trim();
        Optional<User> userOpt = userRepository.findByUsername(identifier);

        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByEmail(identifier.toLowerCase());
        }

        if (userOpt.isEmpty()) {
            return new AuthResponse(false, "Account not found. Please sign up first.");
        }

        User user = userOpt.get();
        boolean matches = false;

        // Check if password is valid BCrypt hash
        if (user.getPassword() != null && user.getPassword().startsWith("$2a$")) {
            matches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        } else {
            // Legacy plain-text password match check
            if (user.getPassword() != null && user.getPassword().equals(request.getPassword())) {
                matches = true;
                // Auto-upgrade legacy plain-text password to BCrypt hash in MySQL database!
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                userRepository.save(user);
            }
        }

        if (!matches) {
            return new AuthResponse(false, "Invalid credentials. Please check your password.");
        }

        // Generate signed JWT token
        String jwtToken = jwtTokenProvider.generateToken(user.getUsername(), user.getEmail(), user.getId());

        return new AuthResponse(true, "Login successful!", user.getId(), user.getUsername(), user.getEmail(), jwtToken);
    }

    /**
     * Changes a user's password. Verifies the old password before setting the new hashed password.
     */
    public AuthResponse changePassword(String username, String oldPassword, String newPassword) {
        if (username == null || username.trim().isEmpty()) {
            return new AuthResponse(false, "Username is required.");
        }
        if (oldPassword == null || oldPassword.isEmpty()) {
            return new AuthResponse(false, "Old password is required.");
        }
        if (newPassword == null || newPassword.length() < 4) {
            return new AuthResponse(false, "New password must be at least 4 characters long.");
        }

        Optional<User> userOpt = userRepository.findByUsername(username.trim());
        if (userOpt.isEmpty()) {
            return new AuthResponse(false, "User not found.");
        }

        User user = userOpt.get();
        boolean matches = false;

        if (user.getPassword() != null && user.getPassword().startsWith("$2a$")) {
            matches = passwordEncoder.matches(oldPassword, user.getPassword());
        } else {
            if (user.getPassword() != null && user.getPassword().equals(oldPassword)) {
                matches = true;
            }
        }

        if (!matches) {
            return new AuthResponse(false, "Incorrect old password.");
        }

        // Set the new password hashed with BCrypt
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return new AuthResponse(true, "Password updated successfully!");
    }
}
