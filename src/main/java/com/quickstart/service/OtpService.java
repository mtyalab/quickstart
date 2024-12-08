package com.quickstart.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class OtpService {

  private static final long OTP_EXPIRY_MINUTES = 5;

  private final Map<String, OtpDetails> otpStore = new HashMap<>();

  public String generateOtp(String phoneNumber) {
      String otp = String.valueOf((int)(Math.random() * 90000) + 10000);
      otpStore.put(phoneNumber, new OtpDetails(otp, LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES)));
      return otp;
  }

  public boolean verifyOtp(String phoneNumber, String otp) {
      OtpDetails otpDetails = otpStore.get(phoneNumber);
      if (otpDetails == null || otpDetails.expiryTime().isBefore(LocalDateTime.now())) {
          return false; // otp not found or expired
      }

      if (otpDetails.otp().equals(otp)) {
          otpStore.remove(phoneNumber); // otp is valid, remove it
          return true;
      }

      return false; // otp is invalid
  }


    private record OtpDetails(String otp, LocalDateTime expiryTime) { }
}
