package com.rooms.rooms.transaction.controller;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class TransactionResolver {

     @QueryMapping(value = "hello")
     public String sayHello(){
          return "Hallo Gaes!!!";
     }


}
