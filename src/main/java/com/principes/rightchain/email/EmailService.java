package com.principes.rightchain.email;

import com.principes.rightchain.utils.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;

    public Boolean validCode(String email, String code) {
        String getCode = (String) redisUtil.get(email);

        return getCode.equals(code);
    }
    public void emailAuthorization(String email) {
        String code = createCode();
        sendMail(email, code);

        redisUtil.set(email, code);
        redisUtil.expire(email, 300);
    }

    private void sendMail(String email, String code) {
        EmailMessage emailMessage = EmailMessage.builder()
                .to(email)
                .subject("[ Right Chain ] 이메일 인증 번호 안내")
                .message("Principes팀의 대구를 빛내는 해커톤 Right Chain 서비스 입니다. 다음의 인증코드를 이용하여 회원가입을 진행해주세요. \n인증번호 : " + code).build();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo());
            mimeMessageHelper.setSubject(emailMessage.getSubject());
            mimeMessageHelper.setText(emailMessage.getMessage(), false);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String createCode() {
        int LENGTH = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < LENGTH; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("MemberService.createCode() exception occur");
            return null;
        }
    }
}