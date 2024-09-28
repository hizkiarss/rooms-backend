package com.rooms.rooms.transaction.service.impl;

import com.rooms.rooms.Booking.Entity.Booking;
import com.rooms.rooms.Booking.Service.BookingService;
import com.rooms.rooms.Booking.dto.CreateBookingDto;
import com.rooms.rooms.email.EmailService;
import com.rooms.rooms.exceptions.AlreadyExistException;
import com.rooms.rooms.exceptions.DataNotFoundException;
import com.rooms.rooms.helper.StringGenerator;
import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.properties.service.PropertiesService;
import com.rooms.rooms.rooms.entity.Rooms;
import com.rooms.rooms.rooms.service.RoomsService;
import com.rooms.rooms.status.entity.Status;
import com.rooms.rooms.status.service.StatusService;
import com.rooms.rooms.transaction.dto.MonthlyTransactionsDto;
import com.rooms.rooms.transaction.dto.TransactionRequest;
import com.rooms.rooms.transaction.dto.TransactionResponse;
import com.rooms.rooms.transaction.entity.Transaction;
import com.rooms.rooms.transaction.entity.TransactionPaymentMethod;
import com.rooms.rooms.transaction.entity.TransactionStatus;
import com.rooms.rooms.transaction.repository.TransactionRepository;
import com.rooms.rooms.transaction.service.TransactionService;
import com.rooms.rooms.transactionDetail.dto.TransactionDetailRequest;
import com.rooms.rooms.transactionDetail.entity.TransactionDetail;
import com.rooms.rooms.transactionDetail.service.TransactionDetailService;
import com.rooms.rooms.users.entity.Users;
import com.rooms.rooms.users.service.UsersService;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Log
@Service
public class TransactionServiceImpl implements TransactionService {
     private TransactionRepository transactionRepository;
     private UsersService usersService;
     private PropertiesService propertiesService;
     private StatusService statusService;
     private TransactionDetailService transactionDetailService;
     private RoomsService roomsService;
     private EmailService emailService;
     private BookingService bookingService;

     public TransactionServiceImpl(
             TransactionRepository transactionRepository,
             UsersService usersService,
             PropertiesService propertiesService,
             StatusService statusService,
             EmailService emailService,
             @Lazy BookingService bookingService,
             @Lazy TransactionDetailService transactionDetailService,
             @Lazy RoomsService roomsService) {
          this.transactionRepository = transactionRepository;
          this.usersService = usersService;
          this.propertiesService = propertiesService;
          this.statusService = statusService;
          this.transactionDetailService = transactionDetailService;
          this.roomsService = roomsService;
          this.emailService = emailService;
          this.bookingService = bookingService;
     }

     @Override
     @Transactional
     public String createTransaction(TransactionRequest req) {
          Transaction newTransaction = req.toTransaction();
          Users users = usersService.getUsersById(req.getUsersId());
          Properties properties = propertiesService.getPropertiesById(req.getPropertiesId());
          TransactionDetailRequest transactionDetailRequest =  req.getTransactionDetailRequests();
          Rooms rooms = roomsService.getRoomsById(req.getTransactionDetailRequests().getRoomId());
          Double price;

          if(req.getPaymentMethod() == TransactionPaymentMethod.bank_transfer){
                price = rooms.getPrice();
          } else {
                price = generateRandomNumber(rooms.getPrice());
          }

          String bookingCode = StringGenerator.generateRandomString(8);
          newTransaction.setFinalPrice(price);
          newTransaction.setUsers(users);
          newTransaction.setProperties(properties);
          newTransaction.setStatus(TransactionStatus.Pending);
          newTransaction.setBookingCode(bookingCode);

          Transaction savedTransaction = transactionRepository.save(newTransaction);
          transactionDetailRequest.setTransactionId(savedTransaction.getId());
          transactionDetailRequest.setPrice(price);
         TransactionDetail savedTransactionDetail =  transactionDetailService.addTransactionDetail(transactionDetailRequest);
          CreateBookingDto bookingDto = new CreateBookingDto();
          bookingDto.setStartDate(savedTransactionDetail.getStartDate());
          bookingDto.setEndDate(savedTransactionDetail.getEndDate());
          bookingDto.setPropertyId(savedTransaction.getProperties().getId());
          bookingDto.setUserId(savedTransaction.getUsers().getId());
          bookingDto.setRoomId(savedTransactionDetail.getRooms().getId());
          bookingDto.setTransactionDetailId(savedTransactionDetail.getId());
          Booking booking = bookingService.createBooking(bookingDto);

          return bookingCode ;
     }


     @Override
     public String cancelTransaction(String bookingCode) {
          Transaction transaction = getTransactionByBookingCode(bookingCode);
          transaction.setStatus(TransactionStatus.Cancelled);
          transactionRepository.save(transaction);
          return "Transaction cancelled";
     }

     public String expireTransaction(String bookingCode) {
          Transaction transaction = getTransactionByBookingCode(bookingCode);
          transaction.setStatus(TransactionStatus.Expired);
          transactionRepository.save(transaction);
          return "Transaction expired";
     }

     @Override
     @Transactional
     public void acceptTransaction(String bookingCode, String signature) {
          Transaction transaction = getTransactionByBookingCode(bookingCode);
          String htmlBody = emailService.getConfirmationEmailTemplate("kmr.oblay96@gmail.com", transaction.getUsers().getUsername(), transaction.getBookingCode(), transaction.getProperties(), transaction.getFirstName(), transaction.getLastName() );
          emailService.sendEmail("kmr.oblay96@gmail.com", "Booking Success! Your Stay is Officially Reserved", htmlBody);
          transaction.setStatus(TransactionStatus.Success);
          transactionRepository.save(transaction);
     }

     @Override
     @Scheduled(cron = "0 0 0 * * ?")
     public void sendCheckInReminder(){
          LocalDate tomorrow = LocalDate.now().plusDays(1);
          List<Transaction> transactions = transactionRepository.findAllByStatusAndDeletedAtIsNull(TransactionStatus.Success);
          for(Transaction transaction : transactions){
               for (TransactionDetail detail : transaction.getTransactionDetails()) {
                    if (detail.getStartDate().isEqual(tomorrow)) {
                         String htmlBody = emailService.getReminderEmailTemplate("kmr.oblay96@gmail.com", transaction.getUsers().getUsername(), transaction.getBookingCode(), transaction.getProperties(), transaction.getFirstName(), transaction.getLastName() );
                         emailService.sendEmail("kmr.oblay96@gmail.com", "Ready for Your Adventure? Time to Check-in Tomorrow!", htmlBody);
                    }
               }
          }
     }

     @Override
     @Scheduled(fixedRate = 60000)
     public void checkPendingTransactions() {
          List<Transaction> transactions = transactionRepository.findAllByStatusAndDeletedAtIsNull(TransactionStatus.Pending);
          for(Transaction transaction : transactions){
               Instant createdAt = transaction.getCreatedAt();
               Instant now = Instant.now();
               Duration duration = Duration.between(createdAt, now);

               if(duration.toHours() >= 1){
                    transaction.setStatus(TransactionStatus.Expired);
                    TransactionDetail transactionDetail = transactionDetailService.getTransactionDetailByTransactionId(transaction.getId());
                    Booking booking = bookingService.getBookingByTransactionDetailId(transactionDetail.getId());
                    bookingService.deleteBookingById(booking.getId());
                    transactionDetailService.deleteTransactionDetailById(transactionDetail.getId());
                    transactionRepository.save(transaction);
                    log.info("Transaction expired");
               }
          }
     }

     @Override
     public void pendingTransaction(String bookingCode) {
          Transaction transaction = getTransactionByBookingCode(bookingCode);
          transaction.setStatus(TransactionStatus.Pending);
          transactionRepository.save(transaction);
     }

     @Override
     public TransactionResponse getTransactionResponseById(Long id) {
          Optional<Transaction> transaction  = Optional.ofNullable(transactionRepository.findByIdAndDeletedAtIsNull(id));
          if(transaction.isEmpty()){
               throw new DataNotFoundException("Transaction with id " + id + " not found");
          }
          TransactionResponse transactionResponse = transaction.get().toTransactionResponse();
          transactionResponse.setUsers(transaction.get().getUsers());
          transactionResponse.setProperties(transaction.get().getProperties());
          transactionResponse.setStatus(transaction.get().getStatus());
          transactionResponse.setTransactionDetails(transaction.get().getTransactionDetails());
          return transactionResponse;
     }

     @Override
     public TransactionResponse getTransactionResponseByBookingCode(String bookingCode) {
          Optional<Transaction> transaction = Optional.ofNullable(transactionRepository.findByBookingCodeAndDeletedAtIsNull(bookingCode));
          if(transaction.isEmpty() || transaction == null){
               throw new DataNotFoundException("Transaction with booking code " + bookingCode + " not found");
          }
          TransactionResponse transactionResponse = transaction.get().toTransactionResponse();
          transactionResponse.setUsers(transaction.get().getUsers());
          transactionResponse.setProperties(transaction.get().getProperties());
          transactionResponse.setStatus(transaction.get().getStatus());
          transactionResponse.setTransactionDetails(transaction.get().getTransactionDetails());
          return transactionResponse;
     }

     @Override
     public Transaction getTransactionById(Long id) {
          Optional<Transaction> transaction  = Optional.ofNullable(transactionRepository.findByIdAndDeletedAtIsNull(id));
          if(transaction.isEmpty()){
               throw new DataNotFoundException("Transaction with id " + id + " not found");
          }
          return transaction.orElse(null );
     }

     @Override
     public Transaction getTransactionByBookingCode(String bookingCode) {
          Optional<Transaction> transaction = Optional.ofNullable(transactionRepository.findByBookingCodeAndDeletedAtIsNull(bookingCode));
          if(transaction.isEmpty()){
               throw new DataNotFoundException("Transaction with id " + bookingCode + " not found");
          }
          return transaction.orElse(null );
     }

     @Override
     public String updateTransactionStatus(Long transactionId, TransactionStatus status){
          Optional<Transaction> transaction  = Optional.ofNullable(transactionRepository.findByIdAndDeletedAtIsNull(transactionId));
          if(transaction.isEmpty() || transaction == null){
               throw new DataNotFoundException("Transaction with id " + transactionId + " not found");
          }
          transaction.get().setStatus(status);
          transactionRepository.save(transaction.get());
          return "Transaction updated successfully";
     }

     @Override
     public List<TransactionResponse> getTransactionByStatus(TransactionStatus status) {
          List<Transaction> transactions = transactionRepository.findAllByStatusAndDeletedAtIsNull(status);

          if (transactions == null || transactions.isEmpty()) {
               throw new DataNotFoundException("Transaction with Status  " + status + " not found");
          }

          return transactions.stream().map(this::toTransactionResponse).collect(Collectors.toList());
     }

     @Override
     public List<TransactionResponse> getTransactionByUsersId(Long id) {
          List<Transaction> transactions = transactionRepository.findAllByUsersIdAndDeletedAtIsNull(id);

          if (transactions == null || transactions.isEmpty()) {
               throw new DataNotFoundException("Transaction with Status id  " + id + " not found");
          }

          return transactions.stream().map(this::toTransactionResponse).collect(Collectors.toList());
     }

     @Override
     public List<TransactionResponse> getTransactionByPropertyId(Long id) {
          List<Transaction> transactions = transactionRepository.findAllByPropertiesIdAndDeletedAtIsNull(id);

          if (transactions == null || transactions.isEmpty()) {
              // throw new DataNotFoundException("Transaction with Status id  " + id + " not found");
               return Collections.emptyList();
          }

          return transactions.stream().map(this::toTransactionResponse).collect(Collectors.toList());
     }

     @Override
     public List<TransactionResponse> getTransactionByPropertyId(Long propertyId, LocalDate startDate, LocalDate endDate) {
          Properties properties = propertiesService.getPropertiesById(propertyId);
          List<Transaction> transactions;

          if (startDate != null && endDate != null) {
               Instant startInstant = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
               Instant endInstant = endDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
               transactions = transactionRepository.findAllByPropertiesIdAndDeletedAtIsNullAndCreatedAtBetween(properties.getId(), startInstant, endInstant);
          } else {
               transactions = transactionRepository.findAllByPropertiesIdAndDeletedAtIsNull(properties.getId());
          }

          if (transactions == null || transactions.isEmpty()) {
               return Collections.emptyList();
          }

          return transactions.stream().map(this::toTransactionResponse).collect(Collectors.toList());
     }

     @Override
     public List<TransactionResponse> getAllTransaction(){
          List<Transaction> transactions = transactionRepository.findAllByDeletedAtIsNull();
          return transactions.stream().map(this::toTransactionResponse).collect(Collectors.toList());
     }

     @Override
     public BigDecimal getTotalRevenueByPropertyId(Long propertyId, LocalDate startDate, LocalDate endDate){
          Properties properties = propertiesService.getPropertiesById(propertyId);
          Instant startInstant = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
          Instant endInstant = endDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
          return transactionRepository.getTotalRevenueByPropertyId(properties.getId(), startInstant, endInstant);
     }

     @Override
     public Integer getTotalTransactionsByPropertyId(Long propertyId, LocalDate startDate, LocalDate endDate){
          Properties properties = propertiesService.getPropertiesById(propertyId);
          Instant startInstant = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
          Instant endInstant = endDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
          return transactionRepository.countTotalTransactionsByPropertyId(properties.getId(), startInstant, endInstant);
     }

     @Override
     public List<MonthlyTransactionsDto> getMonthlyTransactionsByPropertyId(Long propertyId){
          Properties properties = propertiesService.getPropertiesById(propertyId);
          Year currentYear = Year.now();
          List<MonthlyTransactionsDto> overview = new ArrayList<>();
          for (Month month : Month.values()) {
               LocalDate firstDayOfMonth = currentYear.atMonth(month).atDay(1);
               LocalDate lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());


               Instant startInstant = firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant();
               LocalDateTime lastDayOfMonthEnd = lastDayOfMonth.atTime(23, 59, 59);
               Instant endInstant = lastDayOfMonthEnd.atZone(ZoneId.systemDefault()).toInstant();

               Integer totalTransactions = transactionRepository.countTotalTransactionsByPropertyId(properties.getId(), startInstant, endInstant);
               MonthlyTransactionsDto monthlyTransactionsDto = new MonthlyTransactionsDto();
               monthlyTransactionsDto.setMonth(month.toString());
               monthlyTransactionsDto.setTotalTransactions(totalTransactions);
               overview.add(monthlyTransactionsDto);
          }
          return overview;
     }

     public List<Transaction> getLatestTransactionsByPropertyId(Long propertyId){
          Properties properties = propertiesService.getPropertiesById(propertyId);
          List<Transaction> transactions = transactionRepository.findTop5ByStatusAndPropertiesIdAndDeletedAtIsNullOrderByCreatedAtDesc(TransactionStatus.Success, properties.getId());
          return transactions;
     }

     private TransactionResponse toTransactionResponse(Transaction transaction){
          TransactionResponse transactionResponse = new TransactionResponse();
          transactionResponse.setBookingCode(transaction.getBookingCode());
          transactionResponse.setId(transaction.getId());
          transactionResponse.setPaymentMethod(transaction.getPaymentMethod());
          transactionResponse.setFinalPrice(transaction.getFinalPrice());
          transactionResponse.setUsers(transaction.getUsers());
          transactionResponse.setStatus(transaction.getStatus());
          transactionResponse.setProperties(transaction.getProperties());
          transactionResponse.setFirstName(transaction.getFirstName());
          transactionResponse.setLastName(transaction.getLastName());
          transactionResponse.setMobileNumber(transaction.getMobileNumber());
          transactionResponse.setTransactionDetails(transaction.getTransactionDetails());
          transactionResponse.setPaymentProofs(transaction.getPaymentProofs());
          transactionResponse.setReviews(transaction.getReviews());
          transactionResponse.setCreatedAt(transaction.getCreatedAt());
          return transactionResponse;
     }

     private Double generateRandomNumber(Double price){
          Random random = new Random();
          int randomNumber = random.nextInt(999);
          return price + randomNumber;
     }
}
