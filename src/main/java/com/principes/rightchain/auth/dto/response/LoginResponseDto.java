package com.principes.rightchain.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.principes.rightchain.account.entity.Account;
import com.principes.rightchain.account.entity.Role;
import com.principes.rightchain.auth.dto.TokenDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LoginResponseDto {
    private String email;
    private String name;
    private Role role;

    @JsonProperty("token")
    private TokenDto tokenDto;

    public LoginResponseDto(Account account, TokenDto tokenDto) {
        this.email = account.getEmail();
        this.name = account.getName();
        this.role = account.getRole();
        this.tokenDto = tokenDto;
    }
}
