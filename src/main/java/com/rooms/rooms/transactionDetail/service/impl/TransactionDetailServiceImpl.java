package com.rooms.rooms.transactionDetail.service.impl;

import com.rooms.rooms.rooms.entity.Rooms;
import com.rooms.rooms.rooms.service.RoomsService;
import com.rooms.rooms.transaction.entity.Transaction;
import com.rooms.rooms.transaction.service.TransactionService;
import com.rooms.rooms.transactionDetail.dto.TransactionDetailRequest;
import com.rooms.rooms.transactionDetail.entity.TransactionDetail;
import com.rooms.rooms.transactionDetail.repository.TransactionDetailRepository;
import com.rooms.rooms.transactionDetail.service.TransactionDetailService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionDetailServiceImpl implements TransactionDetailService {
     private TransactionDetailRepository transactionDetailRepository;
     private TransactionService transactionService;
     private RoomsService roomsService;
     public TransactionDetailServiceImpl(TransactionDetailRepository transactionDetailRepository, TransactionService transactionService, RoomsService roomsService) {
          this.transactionDetailRepository = transactionDetailRepository;
          this.transactionService = transactionService;
          this.roomsService = roomsService;
     }

     @Override
     public TransactionDetail addTransactionDetail(TransactionDetailRequest req) {
          TransactionDetail transactionDetail = req.toTransactionDetail();
          Transaction transaction = transactionService.getTransactionById(req.getTransactionId());
          Rooms room = roomsService.getRoomsById(req.getRoomId());
          List<Rooms> availableRooms = roomsService.getAvailableRooms(transactionDetail.getStartDate(), transactionDetail.getEndDate(), transaction.getProperties().getId());
          Rooms selectedRoom = roomsService.getRandomRoomByName(availableRooms, room.getName());
          Double price = req.getPrice();
          transactionDetail.setTransaction(transaction);
          transactionDetail.setRooms(selectedRoom);
          transactionDetail.setPrice(price);
         return transactionDetailRepository.save(transactionDetail);
     }
}
