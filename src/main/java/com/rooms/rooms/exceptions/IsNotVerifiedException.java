package com.rooms.rooms.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@EqualsAndHashCode(callSuper = false)
@Setter
@Data
public class IsNotVerifiedException extends RuntimeException {
    public IsNotVerifiedException(String message){
        super(message);
    }
}
