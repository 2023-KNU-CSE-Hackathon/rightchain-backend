package com.principes.rightchain.account.service;

import com.principes.rightchain.account.entity.Account;
import com.principes.rightchain.account.repository.AccountRepository;
import com.principes.rightchain.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Boolean isAccountByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }
}
