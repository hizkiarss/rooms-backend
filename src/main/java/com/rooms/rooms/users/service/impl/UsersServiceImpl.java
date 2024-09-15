package com.rooms.rooms.users.service.impl;

import com.rooms.rooms.auth.entity.RoleName;
import com.rooms.rooms.auth.repository.RedisRepository;
import com.rooms.rooms.email.EmailService;
import com.rooms.rooms.exceptions.AlreadyExistException;
import com.rooms.rooms.exceptions.DataNotFoundException;
import com.rooms.rooms.exceptions.InvalidPasswordException;
import com.rooms.rooms.users.dto.*;
import com.rooms.rooms.users.entity.Gender;
import com.rooms.rooms.users.entity.Users;
import com.rooms.rooms.users.repository.UsersRepository;
import com.rooms.rooms.users.service.UsersService;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Log
@Service
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisRepository redisRepository;
    private final EmailService emailService;

    public UsersServiceImpl(UsersRepository usersRepository, PasswordEncoder passwordEncoder, RedisRepository redisRepository, EmailService emailService) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.redisRepository = redisRepository;
        this.emailService = emailService;
    }

    @Override
    public Users getUsersById(Long id) {
        Optional<Users> users = usersRepository.findById(id);
        return users.orElseThrow(() -> new DataNotFoundException("User not found"));
    }


    @Transactional
    @Override
    public String register(RegisterRequestDto dto, RoleName role) {
        if (usersRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new AlreadyExistException("Email is already in use");
        }
        if (redisRepository.getVerificationLink(dto.getEmail()) != null) {
            throw new AlreadyExistException("You have already registered this email. " +
                    "Please verify your email first or wait for 5 minutes for a new verification link ");
        }
        redisRepository.saveUnverifiedUsers(dto, role);

        String tokenValue = UUID.randomUUID().toString();
        redisRepository.saveVerificationLink(dto.getEmail(), tokenValue);

//        String verificationUrl = dto.getEmail() + tokenValue;
        String htmlBody = emailService.getVerificationEmailTemplate(dto.getEmail(), tokenValue);
        emailService.sendEmail(dto.getEmail(), "Verify Your Account", htmlBody);

        return "Please verify your email to finish this process in less than 5 minutes.";
    }

    @Override
    public RegisterResponseDto verifyEmail(String email) {
        if (redisRepository.getUnverifiedUser(email).toEntity() == null) {
            throw new DataNotFoundException("Please register your account again.");
        }
        Users unverifiedUsers = redisRepository.getUnverifiedUser(email).toEntity();
        unverifiedUsers.setIsVerified(true);
        unverifiedUsers.setPassword(passwordEncoder.encode(unverifiedUsers.getPassword()));
        Users verifiedUsers = usersRepository.save(unverifiedUsers);
        redisRepository.deleteVerificationLink(email);
        return Mapper.entityToDto(verifiedUsers);
    }

    @Override
    public String sendResetPasswordLink(String email) {
        Optional<Users> userData = usersRepository.findByEmail(email);
        if (userData.isEmpty()) {
            return "Not Registered";
        }
        if (!userData.get().getIsVerified()) {
            return "Not Verified";
        }
        if (redisRepository.isResetPasswordLinkValid(email)) {
            redisRepository.deleteResetPasswordLink(email);
        }
        String tokenValue = UUID.randomUUID().toString();
        redisRepository.saveResetPasswordLink(email, tokenValue);
        String htmlBody = emailService.getResetEmailTemplate(tokenValue, email);
        emailService.sendEmail(email, "Reset Password Link", htmlBody);
        return "Please check your email to reset your password";
    }

    @Override
    public String resetPassword(String email, ResetPasswordDto dto) {
        if (redisRepository.getResetPasswordLink(email) == null) {
            throw new DataNotFoundException("Reset Password Link has expired.");
        }
        ;
        Users currentUser = usersRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("User not found"));
        if (!passwordEncoder.matches(dto.getOldPassword(), currentUser.getPassword())) {
            throw new InvalidPasswordException("Invalid password");
        }
        currentUser.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        usersRepository.save(currentUser);
        return "You have been successfully verified your password";
    }


    @Override
    public Users findByEmail(String email) {
        return usersRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("User not found"));
    }

    @Override
    public Users findByEmailGoogleAuth(String email) {
        Optional<Users> users = usersRepository.findByEmail(email);
        return users.orElse(null);
    }

    @Override
    public void deleteUserByEmail(String email, String password) {
        Users currentUser = usersRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("User not found"));
        if (!passwordEncoder.matches(password, currentUser.getPassword())) {
            throw new InvalidPasswordException("Invalid password provided");
        }
        currentUser.setDeletedAt(Instant.now());
        usersRepository.save(currentUser);
    }

    @Override
    public Users save(Users user) {
        return usersRepository.save(user);
    }

    @Override
    public Users uploadAvatar(String email, String imgUrl) {
        log.info("ini email" + email);
        Users currentUser = usersRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("User not found"));
        currentUser.setProfilePicture(imgUrl);
        return usersRepository.save(currentUser);
    }


    @Override
    public Users updateUserInformation(UpdateUserDto dto, String email) {
        Users currentUser = usersRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("User not found"));
        currentUser.setMobileNumber(dto.getMobileNumber());
        currentUser.setUsername(dto.getName());
        if (dto.getGender() == null) {
            currentUser.setGender(null);
        }
        currentUser.setGender(dto.getGender());
        currentUser.setDateOfBirth(dto.getDateOfBirth());
        return usersRepository.save(currentUser);
    }


}

