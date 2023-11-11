package com.principes.rightchain.config;

import com.principes.rightchain.account.repository.AccountRepository;
import com.principes.rightchain.security.filter.JwtAuthenticationFilter;
import com.principes.rightchain.security.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final AccountRepository accountRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, HandlerMappingIntrospector introspection) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable);

        httpSecurity
                .authorizeRequests()
                    .antMatchers("/api/v1/reports/**").hasAnyRole("USER", "TEACHER", "COMMITTEE")
                    .antMatchers("/api/v1/chains/**").hasAnyRole("USER", "TEACHER", "COMMITTEE")
                    .antMatchers("/api/v1/agrees/**").hasAnyRole("USER", "TEACHER", "COMMITTEE")
                    .antMatchers("/api/v1/wallets/**").hasAnyRole("USER", "TEACHER", "COMMITTEE")
                    .antMatchers("/api/v1/auth/**", "/swagger-ui/**").permitAll();

        httpSecurity
                .addFilterBefore(new JwtAuthenticationFilter(accountRepository, jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .httpBasic(withDefaults());

        return httpSecurity.build();
    }
}
