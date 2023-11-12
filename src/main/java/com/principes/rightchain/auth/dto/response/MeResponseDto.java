package com.principes.rightchain.auth.dto.response;

import com.principes.rightchain.account.entity.Role;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeResponseDto {
    private String email;
    private String name;
    private Role role;
    private String schoolName;

    @Builder
    public MeResponseDto(String email, String name, Role role, String schoolName) {
        this.email = email;
        this.name = name;
        this.role = role;
        this.schoolName = schoolName;
    }
}
