package com.rooms.rooms.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@EqualsAndHashCode(callSuper = false)
@Setter
@Data
public class AlreadyExistException extends RuntimeException {

     public AlreadyExistException(String message){
          super(message);
     }
}
