package com.rooms.rooms.users.dto;

import com.rooms.rooms.users.entity.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateUserDto {
    private String name;
    private String mobileNumber;
    private LocalDate dateOfBirth;
    private Gender gender;

}
