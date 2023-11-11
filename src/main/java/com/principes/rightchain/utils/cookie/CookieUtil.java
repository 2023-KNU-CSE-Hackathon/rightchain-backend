package com.principes.rightchain.utils.cookie;

import com.principes.rightchain.exception.CookieNotFoundException;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
public class CookieUtil {

    public static final String ACCESS_TOKEN_COOKIE_NAME = "ACCESS_TOKEN_COOKIE";
    private static final int ACCESS_COOKIE_VALIDATION_SECOND = 1000 * 60 * 60 * 30;

    // Access Token
    public ResponseCookie createCookie(String value) {
        return ResponseCookie.from(ACCESS_TOKEN_COOKIE_NAME, value)
                .httpOnly(false)
                .path("/")
                .secure(false)
                .maxAge(ACCESS_COOKIE_VALIDATION_SECOND)
                .build();
    }

    public ResponseCookie getCookie(HttpServletRequest req, String name) {
        Cookie[] findCookies = req.getCookies();
        if (findCookies == null) {
         throw new CookieNotFoundException("전달된 쿠키가 없습니다.");
        }
        for (Cookie cookie : findCookies) {
            if (cookie.getName().equals(name)) {
                return ResponseCookie.from(name, cookie.getValue()).build();
            }
        }

        throw new CookieNotFoundException("쿠키를 찾을 수 없습니다.");
    }
}