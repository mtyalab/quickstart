package com.quickstart.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class SmsSenderService {

   @Value("${TWILIO_ACCOUNT_SID}")
   private String accountSid;
   @Value("${TWILIO_AUTH_TOKEN}")
   private String authToken;
   @Value("${TWILIO_PHONE_NUMBER}")
   private String twilioPhoneNumber;

   Logger logger = LoggerFactory.getLogger(SmsSenderService.class);


   @PostConstruct
   public void initTwilio() {
       Twilio.init(accountSid, authToken);
   }

   public void sendSMS(String message, String phoneNumber) throws Exception {
      try {
         Message.creator(
                 new PhoneNumber(phoneNumber),
                 new PhoneNumber(twilioPhoneNumber),
                 message
         ).create();
      }catch (Exception e) {
        logger.error(e.getMessage(), e);
        throw new Exception("Failed to send SMS");
      }
   }
}
