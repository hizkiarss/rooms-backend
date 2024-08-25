package com.rooms.rooms.payment.service.impl;

import com.rooms.rooms.payment.entity.PaymentRequest;
import com.rooms.rooms.payment.service.PaymentService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
@Log
@Service
public class PaymentServiceImpl implements PaymentService {
     @Value("${midtrans.server.key}")
     private String serverKey;

     @Value("${midtrans.api.url}")
     private String apiUrl;

     @Override
     public String createTransaction(PaymentRequest paymentRequest) {
          RestTemplate restTemplate = new RestTemplate();
          HttpHeaders headers = new HttpHeaders();
          headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((serverKey + ":").getBytes()));
          headers.set("Content-Type", "application/json");
          log.info("ini serverkeynya: " + Base64.getEncoder().encodeToString((serverKey + ":").getBytes()));

          HttpEntity<PaymentRequest> request = new HttpEntity<>(paymentRequest, headers);
          ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);

          return response.getBody();
     }
}
