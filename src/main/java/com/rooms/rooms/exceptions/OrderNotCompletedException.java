package com.rooms.rooms.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@EqualsAndHashCode(callSuper = false)
@Setter
@Data
public class OrderNotCompletedException  extends RuntimeException {
     public OrderNotCompletedException(String message){
          super(message);
     }
}
