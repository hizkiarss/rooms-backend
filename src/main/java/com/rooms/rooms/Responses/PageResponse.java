package com.rooms.rooms.Responses;

import lombok.Data;

import java.util.List;
@Data
public class PageResponse<T> {
     private List<T> content;
     private int pageNumber;
     private int pageSize;
     private long totalElements;
     private int totalPages;

     // Add constructor
     public PageResponse(List<T> content, int pageNumber, int pageSize, long totalElements, int totalPages) {
          this.content = content;
          this.pageNumber = pageNumber;
          this.pageSize = pageSize;
          this.totalElements = totalElements;
          this.totalPages = totalPages;
     }

     // Default constructor
     public PageResponse() {
     }
}
