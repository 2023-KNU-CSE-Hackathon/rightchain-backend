package com.principes.rightchain.auth.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.principes.rightchain.account.entity.Account;
import com.principes.rightchain.account.entity.Role;
import com.principes.rightchain.account.repository.AccountRepository;
import com.principes.rightchain.auth.dto.TokenDto;
import com.principes.rightchain.auth.dto.request.RegisterRequestDto;
import com.principes.rightchain.auth.dto.response.LoginResponseDto;
import com.principes.rightchain.exception.NotFoundException;
import com.principes.rightchain.security.provider.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    private final AccountRepository accountRepository;


    public LoginResponseDto login(String email, String password){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(authentication);

        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("계정이 존재하지 않습니다."));

        return new LoginResponseDto(account, tokenDto);
    }

    @Transactional
    public Long register(RegisterRequestDto requestDto){
        if(accountRepository.existsByEmail(requestDto.getEmail())){
            throw new IllegalStateException("중복된 이메일 입니다.");
        }

        Role role = switch (requestDto.getRole()) {
            case "학부모/학생" -> Role.ROLE_USER;
            case "선생님" -> Role.ROLE_TEACHER;
            case "위원회" -> Role.ROLE_COMMITTEE;
            default -> throw new IllegalStateException("알 수 없는 역할 입니다.");
        };

        Account account = accountRepository.save(requestDto.toEntity(passwordEncoder, role));
        return account.getId();
    }

    public boolean isAccountByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }
}
