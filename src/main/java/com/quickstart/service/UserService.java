package com.quickstart.service;

import com.quickstart.dto.UserDetailsDTO;
import com.quickstart.model.User;
import com.quickstart.repo.UserRepo;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.Optional;
import java.util.Random;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final JavaSmtpGmailSenderService emailSenderService;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepo userRepo, JavaSmtpGmailSenderService emailSenderService, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.emailSenderService = emailSenderService;
        this.passwordEncoder = passwordEncoder;
    }


    public void save(User user) throws Exception {
        if (user == null ||
                StringUtils.isBlank(user.getUsername()) ||
                StringUtils.isBlank(user.getFirstName()) ||
                        StringUtils.isBlank(user.getLastName()) ||
                       StringUtils.isBlank(user.getPhoneNumber()) ||
                      StringUtils.isBlank(user.getRole()) ||
                      StringUtils.isBlank(user.getPassword())) {
            throw new Exception("All fields are required!!!");
        }
        Optional<User> isUserExists = userRepo.findByUsername(user.getUsername());
        if(isUserExists.isPresent()) {
          logger.info("User already exists");
            throw new Exception("User already exists!!!");
        }
       this.emailSenderService.sendEmail(
               user.getUsername(),
               "Hurray!!!",
               "Account has been successfully created."
               );
       this.userRepo.save(user);
    }

    public void generateAndSendOtp(String username) {
        Optional<User> userOtp = userRepo.findByUsername(username);

        if (userOtp.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOtp.get();
        String otp = String.valueOf(new Random().nextInt(90000) + 10000);
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        userRepo.save(user);

        String subject = "Password Reset Code";
        String message = "Security Code: " + otp;
        emailSenderService.sendEmail(
                user.getUsername(),
                subject,
                message
        );
    }

    public void resetPassword(String username, String otp, String newPassword) {
        Optional<User> userOpt = userRepo.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();
        if (user.getOtp() == null || !user.getOtp().equals(otp) || user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Invalid or expired Security Code");
        }

        emailSenderService.sendEmail(
                username,
                "Congratulations!!!",
                "Password has been successfully changed."
        );

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepo.save(user);
    }

    public UserDetailsDTO getUserByUsername(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new UserDetailsDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getMiddleName(),
                user.getRole(),
                user.getPhoneNumber(),
                user.getUsername());
    }
}
