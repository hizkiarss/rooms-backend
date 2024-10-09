package com.rooms.rooms.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import javax.security.auth.login.LoginException;

@EqualsAndHashCode(callSuper = false)
@Setter
@Data
public class LoginFailedException extends RuntimeException {
    public LoginFailedException(String message) {super(message);}
}
