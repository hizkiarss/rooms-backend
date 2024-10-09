package com.rooms.rooms.review.entity;



import lombok.Data;

import java.util.List;
@Data
public class ReviewPage {
     private List<Review> content;
     private int totalPages;
     private long totalItems;

     public ReviewPage(List<Review> reviews, int totalPages, long totalItems) {
          this.content = reviews;
          this.totalPages = totalPages;
          this.totalItems = totalItems;
     }


}
