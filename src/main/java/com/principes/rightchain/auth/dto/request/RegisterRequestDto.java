package com.principes.rightchain.auth.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.principes.rightchain.account.entity.Account;
import com.principes.rightchain.account.entity.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterRequestDto {
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String role;
    @NotNull
    private String schoolName;

    public Account toEntity(PasswordEncoder passwordEncoder, Role role_){
        return Account.builder()
                .email(this.email)
                .password(passwordEncoder.encode(this.password))
                .schoolName(this.schoolName)
                .role(role_)
                .build();
    }
}