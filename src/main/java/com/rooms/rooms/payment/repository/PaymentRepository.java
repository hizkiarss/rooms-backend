package com.rooms.rooms.payment.repository;

import com.rooms.rooms.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
     Payment findByBookingCode(String bookingCode);
}
