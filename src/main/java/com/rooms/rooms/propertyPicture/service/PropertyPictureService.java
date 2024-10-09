package com.rooms.rooms.propertyPicture.service;

import com.rooms.rooms.propertyPicture.entity.PropertyPicture;

import java.util.List;

public interface PropertyPictureService {
    PropertyPicture addPropertyPicture(String imgUrl, Long propertyId);
    void deletePropertyPicture(Long propertyPictureId, String email);
    List<PropertyPicture> getPropertyPicturesbyPropertyId(Long userId);
}
