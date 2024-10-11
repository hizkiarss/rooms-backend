package com.rooms.rooms.propertyPicture.service;

import com.rooms.rooms.propertyPicture.entity.PropertyPicture;

import java.util.List;

public interface PropertyPictureService {
    String addPropertyPictures(List<String> imgUrls, Long propertyId);
    void deletePropertyPictures(List<Long> propertyPictureIds, String email);
    List<PropertyPicture> getPropertyPicturesbyPropertyId(Long userId);
}
