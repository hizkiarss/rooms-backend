package com.rooms.rooms.propertyPicture.service.impl;

import com.rooms.rooms.exceptions.DataNotFoundException;
import com.rooms.rooms.exceptions.UnauthorizedAccessException;
import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.properties.service.PropertiesService;
import com.rooms.rooms.propertyPicture.entity.PropertyPicture;
import com.rooms.rooms.propertyPicture.repository.PropertyPictureRepository;
import com.rooms.rooms.propertyPicture.service.PropertyPictureService;
import org.springframework.stereotype.Service;
import javax.xml.crypto.Data;
import java.time.Instant;
import java.util.List;


@Service
public class PropertyPictureServiceImpl implements PropertyPictureService {
    private final PropertyPictureRepository propertyPictureRepository;
    private final PropertiesService propertiesService;

    public PropertyPictureServiceImpl(PropertyPictureRepository propertyPictureRepository, PropertiesService propertiesService) {
        this.propertyPictureRepository = propertyPictureRepository;
        this.propertiesService = propertiesService;
    }

    @Override
    public PropertyPicture addPropertyPicture(String imgUrl, Long propertyId) {
        Properties currentProperty = propertiesService.getPropertiesById(propertyId);
        PropertyPicture propertyPicture = new PropertyPicture();
        propertyPicture.setImgUrl(imgUrl);
        propertyPicture.setProperties(currentProperty);
        return propertyPictureRepository.save(propertyPicture);
    }

    @Override
    public void deletePropertyPicture(Long propertyPictureId, String email) {
        Properties currentProperty = propertiesService.findPropertiesByPropertyPicture(propertyPictureId);
        if (currentProperty == null) {
            throw new DataNotFoundException("Property not found");
        }
        PropertyPicture currentPropertyPicture = propertyPictureRepository.findById(propertyPictureId).orElseThrow(() -> new DataNotFoundException("Picture not found"));
        currentPropertyPicture.setDeletedAt(Instant.now());
        propertyPictureRepository.save(currentPropertyPicture);
    }

    @Override
    public List<PropertyPicture> getPropertyPicturesbyPropertyId(Long userId) {
        return propertyPictureRepository.findPropertyPicturesByPropertiesId(userId);
    }
}
