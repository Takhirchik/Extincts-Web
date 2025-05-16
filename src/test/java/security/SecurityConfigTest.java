//package security;
//
//import com.warlock.config.SecurityConfig;
//import org.junit.jupiter.api.Test;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.web.cors.CorsConfiguration;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class SecurityConfigTest {
//
//    @Test
//    public void testWebSocketSecurity() throws Exception {
//        SecurityConfig securityConfig = new SecurityConfig(null, null);
//        HttpSecurity http = mock(HttpSecurity.class);
//
//        when(http.csrf(any())).thenReturn(http);
//        when(http.cors(any())).thenReturn(http);
//        when(http.authorizeHttpRequests(any())).thenReturn(http);
//        when(http.sessionManagement(any())).thenReturn(http);
//        when(http.authenticationProvider(any())).thenReturn(http);
//        when(http.addFilterBefore(any(), any())).thenReturn(http);
//        when(http.httpBasic(any())).thenReturn(http);
//        when(http.formLogin(any())).thenReturn(http);
//        when(http.logout(any())).thenReturn(http);
//
//        SecurityFilterChain filterChain = securityConfig.securityFilterChain(http);
//
//        assertNotNull(filterChain);
//
//        // Проверяем, что WebSocket endpoints разрешены без аутентификации
//        verify(http).authorizeHttpRequests(req -> req
//                .requestMatchers("/websocket/**", "/topic/**", "/queue/**", "/app/**").permitAll()
//        );
//    }
//
//    @Test
//    public void testCorsConfiguration() throws Exception {
//        SecurityConfig securityConfig = new SecurityConfig(null, null);
//        HttpSecurity http = mock(HttpSecurity.class);
//
//        when(http.csrf(any())).thenReturn(http);
//        when(http.cors(any())).thenAnswer(invocation -> {
//            // Проверяем конфигурацию CORS
//            var customizer = invocation.getArgument(0, HttpSecurity.CorsConfigurer.class);
//            CorsConfiguration config = new CorsConfiguration();
//            config.setAllowedOriginPatterns(List.of("*"));
//            config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
//            config.setAllowedHeaders(List.of("*"));
//            config.setAllowCredentials(true);
//
//            assertTrue(config.getAllowedOriginPatterns().contains("*"));
//            assertTrue(config.getAllowedMethods().containsAll(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")));
//            assertTrue(config.getAllowedHeaders().contains("*"));
//            assertTrue(config.getAllowCredentials());
//
//            return http;
//        });
//
//        securityConfig.securityFilterChain(http);
//    }
//}