package com.principes.rightchain.account.service;

import com.principes.rightchain.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Boolean isAccountByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }
}
