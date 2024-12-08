# Spring Boot Quick Start REST App  

A Spring Boot application designed to provide a quick start for building RESTful APIs. This project includes core functionalities like user registration, login, SMS notifications, email support, and robust security measures.  

---

## Features  
- **User Management**:  
  - Registration and login endpoints.  
  - Validation for secure user data handling.  
- **Notifications**:  
  - SMS notifications using Twilio.  
  - Email notifications using Java Mail Sender.  
- **Security**:  
  - Spring Security integration for authentication and authorization.  
  - CSRF protection enabled.  
- **Database**:  
  - MongoDB for scalable and flexible data storage.  
- **Code Simplicity**:  
  - Lombok for reducing boilerplate code.  

---

## Requirements  
- Java 17+  
- Maven  
- MongoDB  
- Twilio Account (for SMS notifications)  
- Email SMTP Server (for sending emails)  

---

## Getting Started  

### Clone the Repository  
```bash  
git clone https://github.com/your-repo/spring-boot-quick-start-rest-app.git  
cd spring-boot-quick-start-rest-app  
```  

### Configure the Application
1. Update the `application.properties` file with your credentials:
   ```properties  
   # MongoDB configuration  
   spring.data.mongodb.uri=mongodb://localhost:27017/your_database  

   # Twilio configuration  
   twilio.account-sid=your_account_sid  
   twilio.auth-token=your_auth_token  
   twilio.phone-number=your_twilio_phone_number  

   # Email configuration  
   spring.mail.host=smtp.example.com  
   spring.mail.port=587  
   spring.mail.username=your_email@example.com  
   spring.mail.password=your_email_password  
   spring.mail.properties.mail.smtp.auth=true  
   spring.mail.properties.mail.smtp.starttls.enable=true  

   # Security  
   spring.security.user.name=admin  
   spring.security.user.password=admin_password  
   ```  

2. Ensure MongoDB is running locally or provide a remote connection URI.

### Build and Run
```bash  
mvn clean install  
mvn spring-boot:run  
```  

---

## API Endpoints

### User Authentication
- **POST** `/api/auth/register` - Register a new user.
- **POST** `/api/auth/login` - Login to get a JWT token.

### Notifications
- **POST** `/api/notifications/sms` - Send an SMS (requires valid Twilio setup).
- **POST** `/api/notifications/email` - Send an email.

### Health Check
- **GET** `/api/health` - Verify the server is running.

---

## Tech Stack
- **Spring Boot**
- **Spring Security**
- **MongoDB**
- **Twilio SDK**
- **Java Mail Sender**
- **Lombok**

---

## Contributing
Contributions are welcome! Fork the repository, create a feature branch, and submit a pull request.

---

## License
This project is licensed under the MIT License.

---

## Author
Developed by mtyalab
```  
