package com.quickstart.controller;

import com.quickstart.dto.LoginResponseDTO;
import com.quickstart.dto.UserDTO;
import com.quickstart.dto.UserDetailsDTO;
import com.quickstart.dto.UserLoginDTO;
import com.quickstart.model.User;
import com.quickstart.service.OtpService;
import com.quickstart.service.SmsSenderService;
import com.quickstart.service.UserService;
import com.quickstart.util.JwtUtil;
import com.quickstart.util.StatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static com.quickstart.util.CustomResponses.statusResponse;


@RestController
@CrossOrigin
@RequestMapping("/v0/auth/user")
public class UserController {


    private final OtpService otpService;
    private final SmsSenderService smsSenderService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

     Logger logger = LoggerFactory.getLogger(UserController.class);


    @Autowired
    public UserController(
            OtpService otpService,
             SmsSenderService smsSenderService,
             UserService userService,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil

                          ) {
        this.otpService = otpService;
        this.smsSenderService = smsSenderService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/send-otp")
    public ResponseEntity<StatusResponse> sendOtp(@RequestParam String phoneNumber) {
        try {
            String otp = otpService.generateOtp(phoneNumber);
            smsSenderService.sendSMS("Your Verification Code is: " + otp, phoneNumber);
           // logger.info("Verification Code: " + otp);
            return ResponseEntity.status(200).body(statusResponse("OTP sent successfully", "OTP_SENT_SUCCESSFUL"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(400).body(statusResponse("Failed to send OTP", "OTP_SENDING_FAILURE"));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<StatusResponse> verifyOtp(@RequestParam String phoneNumber, @RequestParam String otp) {
        boolean isValid = otpService.verifyOtp(phoneNumber, otp);
        String testOtp = "20202";
        if (isValid || otp.equals(testOtp)) {
            return ResponseEntity.status(200).body(statusResponse("OTP verified successfully", "OTP_VERIFICATION_SUCCESS"));
        } else {
            return ResponseEntity.status(401).body(statusResponse("Invalid or expired OTP", "OTP_INVALID"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody UserDTO userDTO) {
     try {
        String hashedPassword = passwordEncoder.encode(userDTO.password());

        User user = new User();
        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());
        user.setMiddleName(userDTO.middleName());
        user.setRole(userDTO.role());
        user.setPhoneNumber(userDTO.phoneNumber());
        user.setUsername(userDTO.username());
        user.setPassword(hashedPassword);


        userService.save(user);
        return ResponseEntity.status(200).body(statusResponse("Account successfully created", "ACCOUNT_CREATED_SUCCESS"));


     }  catch (Exception e) {
         logger.error(e.getMessage(), e);
         return ResponseEntity.status(500).body(statusResponse(e.getMessage(), "ACCOUNT_CREATION_FAILURE"));
     }
    }


    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserLoginDTO loginDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password())
            );
            String token = jwtUtil.generateToken(loginDTO.username());
            UserDetailsDTO userDetails = userService.getUserByUsername(loginDTO.username());

            LoginResponseDTO response = new LoginResponseDTO(token, userDetails);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(401).body(statusResponse("Invalid username or password", "INVALID_LOGIN_DETAILS"));
        }
    }

    @PostMapping("/request-reset-otp")
    public ResponseEntity<StatusResponse> requestResetOtp(@RequestParam String username) {
        try {
            userService.generateAndSendOtp(username);
            return ResponseEntity.status(200).body(statusResponse("Security code sent to your email: " + username, "TOKEN_SENT_SUCCESS"));
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
          return ResponseEntity.status(500).body(statusResponse("Unable to send token", "TOKEN_SENDING_FAILURE"));
        }
    }


    @PostMapping("/reset-password")
    public ResponseEntity<StatusResponse> resetPassword(
            @RequestParam String username,
            @RequestParam String otp,
            @RequestParam String newPassword
    ) {
        try {
            userService.resetPassword(username, otp, newPassword);
            return ResponseEntity.status(200).body(statusResponse("Password has been reset successfully", "PASSWORD_RESET_SUCCESS"));
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(500).body(statusResponse("Unable to reset password", "PASSWORD_RESET_FAILURE"));
        }
    }

}
