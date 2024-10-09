package com.rooms.rooms.auth.dto;

import com.rooms.rooms.auth.entity.RoleName;
import lombok.Data;
import org.springframework.graphql.data.method.annotation.SchemaMapping;

@Data
@SchemaMapping(typeName = "AuthPayload")
public class LoginResponseDto {
    private String token;
    private RoleName role;
}
