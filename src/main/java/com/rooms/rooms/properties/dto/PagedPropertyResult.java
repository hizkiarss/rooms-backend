package com.rooms.rooms.properties.dto;


import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PagedPropertyResult {
    private List<FilteredPropertyDto> properties;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;


    public PagedPropertyResult toDto(Page<FilteredPropertyDto> propertyPage, PagedPropertyResult resultDto) {
        if (propertyPage == null) {
            throw new IllegalArgumentException("propertyPage cannot be null");
        }
        if (resultDto == null) {
            resultDto = new PagedPropertyResult();
        }

        resultDto.setProperties(propertyPage.toList());
        resultDto.setTotalElements(propertyPage.getTotalElements());
        resultDto.setTotalPages(propertyPage.getTotalPages());
        resultDto.setCurrentPage(propertyPage.getNumber());
        resultDto.setPageSize(propertyPage.getSize());

        return resultDto;
    }
}