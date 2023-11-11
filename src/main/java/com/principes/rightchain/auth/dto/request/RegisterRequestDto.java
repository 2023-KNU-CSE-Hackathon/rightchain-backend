package com.principes.rightchain.auth.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.principes.rightchain.account.entity.Account;
import com.principes.rightchain.account.entity.Role;
import com.principes.rightchain.account.entity.Sex;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterRequestDto {
    private String email;
    private String password;

    private Role role;

    private int age;
    private Sex sex;
    private String phoneNum;
    private String schoolName;

    public Account toEntity(PasswordEncoder passwordEncoder){
        return Account.builder()
                .email(this.email)
                .password(passwordEncoder.encode(this.password))
                .age(this.age)
                .sex(this.sex)
                .phoneNum(this.phoneNum)
                .schoolName(this.schoolName)
                .role(this.role)
                .build();
    }
}