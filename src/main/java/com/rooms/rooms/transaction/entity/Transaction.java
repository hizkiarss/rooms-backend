package com.rooms.rooms.transaction.entity;

import com.rooms.rooms.bedTypes.entity.BedTypes;
import com.rooms.rooms.paymentProof.entity.PaymentProof;
import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.review.entity.Review;
import com.rooms.rooms.status.entity.Status;
import com.rooms.rooms.transaction.dto.TransactionRequest;
import com.rooms.rooms.transaction.dto.TransactionResponse;
import com.rooms.rooms.transactionDetail.entity.TransactionDetail;
import com.rooms.rooms.users.entity.Users;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "transactions")
public class Transaction implements Serializable {

     @Id
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transactions_id_gen")
     @SequenceGenerator(name = "transactions_id_gen", sequenceName = "transactions_id_seq", allocationSize = 1)
     @Column(name = "id", nullable = false)
     private Long id;

     @ManyToOne(fetch = FetchType.LAZY, optional = false)
     @JoinColumn(name = "user_id")
     private Users users;

     @ManyToOne(fetch = FetchType.LAZY, optional = false)
     @JoinColumn(name = "property_id")
     private Properties properties;

     @Column(name = "first_name", nullable = false)
     private String firstName;

     @Column(name = "last_name", nullable = false)
     private String lastName;

     @Column(name = "mobile_number", nullable = false)
     private String mobileNumber;

     @Column(name = "booking_code", nullable = false)
     private String bookingCode;

     @Enumerated(EnumType.STRING)
     @Column(name = "status", nullable = false)
     private TransactionStatus status;

     @Column(name = "final_price", nullable = false)
     private Double finalPrice;

     @Enumerated(EnumType.STRING)
     @Column(name = "payment_method", nullable = false)
     private TransactionPaymentMethod paymentMethod;

     @OneToMany(mappedBy = "transaction", fetch = FetchType.EAGER)
     private List<TransactionDetail> transactionDetails;

     @OneToMany(mappedBy = "transaction", fetch = FetchType.EAGER)
     private List<PaymentProof> paymentProofs;

     @OneToMany(mappedBy = "transaction", fetch = FetchType.EAGER)
     private List<Review> reviews;

     @ColumnDefault("CURRENT_TIMESTAMP")
     @Column(name = "created_at")
     private Instant createdAt = Instant.now();

     @ColumnDefault("CURRENT_TIMESTAMP")
     @Column(name = "updated_at")
     private Instant updatedAt = Instant.now();

     @Column(name = "deleted_at")
     private Instant deletedAt;

     @PrePersist
     void onSave() {
          this.createdAt = Instant.now();
          this.updatedAt = Instant.now();
     }

     @PreUpdate
     void onUpdate() {
          this.updatedAt = Instant.now();
     }

     @PreDestroy
     void onDelete() {
          this.deletedAt = Instant.now();
     }

     public TransactionResponse toTransactionResponse(){
         TransactionResponse transactionResponse = new TransactionResponse();
          transactionResponse.setId(this.id);
          transactionResponse.setFinalPrice(this.finalPrice);
          transactionResponse.setPaymentMethod(this.paymentMethod);
          transactionResponse.setStatus(this.status);
          transactionResponse.setFirstName(this.firstName);
          transactionResponse.setLastName(this.lastName);
          transactionResponse.setMobileNumber(this.mobileNumber);
          transactionResponse.setBookingCode(this.bookingCode);
          return transactionResponse;
     }
}
