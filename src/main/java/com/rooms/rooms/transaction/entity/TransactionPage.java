package com.rooms.rooms.transaction.entity;

import com.rooms.rooms.Responses.PageResponse;
import com.rooms.rooms.transaction.dto.TransactionResponse;
import lombok.Data;

import java.util.List;

@Data
public class TransactionPage {
     private List<TransactionResponse> content;
     private int pageNumber;
     private int pageSize;
     private long totalElements;
     private int totalPages;

     public TransactionPage(PageResponse<TransactionResponse> pageResponse) {
          this.content = pageResponse.getContent();
          this.pageNumber = pageResponse.getPageNumber();
          this.pageSize = pageResponse.getPageSize();
          this.totalElements = pageResponse.getTotalElements();
          this.totalPages = pageResponse.getTotalPages();
     }
}