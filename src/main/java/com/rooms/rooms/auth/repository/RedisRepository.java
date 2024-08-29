package com.rooms.rooms.auth.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rooms.rooms.auth.entity.RoleName;
import com.rooms.rooms.users.dto.RegisterRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisRepository {
    private static final String STRING_KEY_PREFIX = "Rooms:jwt:strings:";
    private static final String STRING_BLACKLIST_KEY_PREFIX = "Rooms:blacklist-jwt:strings:";
    private static final String STRING_LINK_VERIFICATION_KEY_PREFIX = "Rooms:link:strings:";
    private static final String STRING_LINK_RESET_PASSWORD_KEY_PREFIX = "Rooms:reset-password:strings:";
    private static final String STRING_LINK_UNVERIFIED_USERS_PREFIX = "Rooms:unverified-users:strings:";
    private final ObjectMapper objectMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final ValueOperations<String, String> valueOps;

    @Autowired
    public RedisRepository(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper, StringRedisTemplate stringRedisTemplate) {
        this.objectMapper = objectMapper;
        this.stringRedisTemplate = stringRedisTemplate;
        this.valueOps = stringRedisTemplate.opsForValue();
    }

    public void saveJwtKey(String email, String jwtKey) {
        valueOps.set(STRING_KEY_PREFIX + email, jwtKey, 1, TimeUnit.HOURS);
    }

    public void saveVerificationLink(String email, String token) {
        valueOps.set(STRING_LINK_VERIFICATION_KEY_PREFIX + email, token, 5, TimeUnit.MINUTES);
    }


    public void saveUnverifiedUsers(RegisterRequestDto dto, RoleName roleName) {
        try {
            // Create a map to hold user data
            Map<String, String> userData = new HashMap<>();
            userData.put("email", dto.getEmail());
            userData.put("username", dto.getUsername());
            userData.put("password", dto.getPassword());
            userData.put("mobileNumber", dto.getMobileNumber());
            userData.put("role", roleName.toString());

            String json = objectMapper.writeValueAsString(userData);

            stringRedisTemplate.opsForValue().set(
                    STRING_LINK_UNVERIFIED_USERS_PREFIX + dto.getEmail(), // Use email as the key
                    json,
                    5,
                    TimeUnit.MINUTES
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RegisterRequestDto getUnverifiedUser(String email) {
        // Fetch JSON string from Redis
        String json = stringRedisTemplate.opsForValue().get(STRING_LINK_UNVERIFIED_USERS_PREFIX + email);

        if (json != null) {
            try {
                return objectMapper.readValue(json, RegisterRequestDto.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public Boolean isResetPasswordLinkValid(String email) {
        return valueOps.get(STRING_LINK_RESET_PASSWORD_KEY_PREFIX + email) != null;
    }

    public void saveResetPasswordLink(String email, String token) {
        valueOps.set(STRING_LINK_RESET_PASSWORD_KEY_PREFIX + email, token, 1, TimeUnit.HOURS);
    }

    public void deleteResetPasswordLink(String email) {
        valueOps.getOperations().delete(STRING_LINK_RESET_PASSWORD_KEY_PREFIX + email);
    }


    public Boolean isVerificationLinkValid(String email) {
        return valueOps.get(STRING_LINK_VERIFICATION_KEY_PREFIX + email) != null;
    }

    public String getVerificationLink(String email) {
        return valueOps.get(STRING_LINK_VERIFICATION_KEY_PREFIX + email);
    }

    public String getResetPasswordLink(String email) {
        return valueOps.get(STRING_LINK_RESET_PASSWORD_KEY_PREFIX + email);
    }

    public String getJwtKey(String email) {
        return valueOps.get(STRING_KEY_PREFIX + email);
    }

    public void deleteJwtKey(String email) {
        valueOps.getOperations().delete(STRING_KEY_PREFIX + email);
    }


    public void deleteVerificationLink(String email) {
        valueOps.getOperations().delete(STRING_LINK_VERIFICATION_KEY_PREFIX + email);
    }

    public void blackListJwt(String email, String jwt) {
        valueOps.set(STRING_BLACKLIST_KEY_PREFIX + jwt, email, 1, TimeUnit.HOURS);
    }

    public Boolean isKeyBlacklisted(String jwt) {
        return valueOps.get(STRING_BLACKLIST_KEY_PREFIX + jwt) != null;
    }
}

