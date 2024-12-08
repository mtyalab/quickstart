package com.quickstart.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
public class User {
    @Id
    private String id;
    @Field(name = "first_name")
    private String firstName;
    @Field(name = "last_name")
    private String lastName;
    @Field(name = "middle_name")
    private String middleName;
    private String role;
    @Field(name = "phone_number")
    private String phoneNumber;
    private String username;
    private String password;

    @CreatedDate
    @Field(name = "date_created")
    private LocalDateTime dateCreated;

    private String otp;


    @Field(name = "otp_expiry")
    private LocalDateTime otpExpiry;

}
