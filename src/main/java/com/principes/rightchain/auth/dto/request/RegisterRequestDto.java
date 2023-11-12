package com.principes.rightchain.auth.dto.request;

import javax.validation.constraints.NotNull;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.principes.rightchain.account.entity.Account;
import com.principes.rightchain.account.entity.Role;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterRequestDto {
    @NotNull
    private String email;
    @NotNull @Setter
    private String password;
    @NotNull
    private String role;
    @NotNull
    private String name;
    @NotNull
    private String schoolName;

    public Account toEntity(PasswordEncoder passwordEncoder, Role role_){
        return Account.builder()
                .email(this.email)
                .password(passwordEncoder.encode(this.password))
                .schoolName(this.schoolName)
                .name(name)
                .role(role_)
                .build();
    }
}