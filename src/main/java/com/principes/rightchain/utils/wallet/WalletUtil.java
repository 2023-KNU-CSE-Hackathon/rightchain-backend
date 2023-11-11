package com.principes.rightchain.utils.wallet;

import org.springframework.stereotype.Component;

@Component
public class WalletUtil {
    public String getWalletName(String caseNum) {
        return "[사건번호/" + caseNum + "]";
    }
}
