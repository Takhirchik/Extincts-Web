package com.warlock.service;

import com.warlock.domain.User;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final RoleService roleService;
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    private static final String DEFAULT_ROLE = "ROLE_USER";


    /**
     * Регистрация пользователя
     *
     * @param request объект User
     * @return jwtToken
     */
    @Override
    @Transactional
    public @NonNull String register(@NonNull User request) {

        request.setRole(roleService.findByName(DEFAULT_ROLE));
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        userService.create(request);

        return jwtService.generateToken(request);
    }

    /**
     * Аутентификация пользователя
     *
     * @param request объект User
     * @return jwtToken
     */
    @Override
    public @NonNull String authenticate(@NonNull User request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        var user = userService.userDetailsService().loadUserByUsername(request.getUsername());
        return jwtService.generateToken(user);
    }
}
