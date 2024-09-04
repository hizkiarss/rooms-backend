package com.rooms.rooms.helper;

import java.util.Random;

public class StringGenerator {
     public static String generateRandomString(int length) {
          String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
          Random random = new Random();
          StringBuilder randomString = new StringBuilder(length);

          for (int i = 0; i < length; i++) {
               int index = random.nextInt(characters.length());
               randomString.append(characters.charAt(index));
          }

          return randomString.toString();
     }
}
