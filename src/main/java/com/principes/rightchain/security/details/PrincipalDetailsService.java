package com.principes.rightchain.security.details;

import com.principes.rightchain.account.entity.Account;
import com.principes.rightchain.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account =  accountRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("계정을 찾을 수 없습니다."));

        return new PrincipalDetails(account);
    }
}
