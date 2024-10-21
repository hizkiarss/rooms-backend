package com.rooms.rooms.rooms.dto;

import com.rooms.rooms.exceptions.DataNotFoundException;
import com.rooms.rooms.rooms.entity.Rooms;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PagedRoomResult{
    private List<Rooms> rooms;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;


    public PagedRoomResult toDto(Page<Rooms> roomsPage, PagedRoomResult resultDto) {
        if (roomsPage == null) {
            throw new IllegalArgumentException("propertyPage cannot be null");
        }
        if (resultDto == null) {
            resultDto = new PagedRoomResult();
        }

        resultDto.setRooms(roomsPage.toList());
        resultDto.setTotalElements(roomsPage.getTotalElements());
        resultDto.setTotalPages(roomsPage.getTotalPages());
        resultDto.setCurrentPage(roomsPage.getNumber());
        resultDto.setPageSize(roomsPage.getSize());
        return resultDto;
    }
}