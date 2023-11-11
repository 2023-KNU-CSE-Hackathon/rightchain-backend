package com.principes.rightchain.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.principes.rightchain.account.entity.Role;
import com.principes.rightchain.auth.dto.TokenDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LoginResponseDto {
    private String email;
    private Role role;

    @JsonProperty("token")
    private TokenDto tokenDto;

    @Builder
    public LoginResponseDto(String email, Role role, TokenDto tokenDto) {
        this.email = email;
        this.role = role;
        this.tokenDto = tokenDto;
    }
}
