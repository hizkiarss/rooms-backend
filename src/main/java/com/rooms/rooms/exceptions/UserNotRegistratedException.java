package com.rooms.rooms.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@EqualsAndHashCode(callSuper = false)
@Setter
@Data
public class UserNotRegistratedException extends RuntimeException {
    public UserNotRegistratedException(String message) {
        super(message);
    }
}
