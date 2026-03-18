package br.com.backend.service;

public class EmailServiceImpl implements EmailService {

    @Override
    public void sendResetPasswordEmail(String token, String email) {
        System.out.println("Reset password token" + token);
    }
}
