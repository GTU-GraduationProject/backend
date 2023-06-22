package com.backend.recognitionitems.auth.controller;

import com.backend.recognitionitems.auth.jwt.JwtUtil;
import com.backend.recognitionitems.auth.dto.AuthRequestDto;
import com.backend.recognitionitems.auth.dto.AuthResponseDto;
import com.backend.recognitionitems.auth.security.CustomUserDetailsService;
import com.backend.recognitionitems.user.dto.User;
import com.backend.recognitionitems.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthRestController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> createToken(@RequestBody AuthRequestDto authRequestDto) throws Exception {
        if (!authRequestDto.getUsername().chars().allMatch(Character::isDigit)) {
            throw new Exception("Username does not just contain numbers");
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequestDto.getUsername(),
                    authRequestDto.getPassword()));

        } catch (BadCredentialsException ex) {
            throw new Exception("Incorrect username or password", ex);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequestDto.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        final User user = userService.getUser(authRequestDto.getUsername());
        return ResponseEntity.ok(AuthResponseDto.builder()
                .token(jwt)
                .username(user.getId().toString())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .role(user.getRole().toString())
                .build());
    }
}
