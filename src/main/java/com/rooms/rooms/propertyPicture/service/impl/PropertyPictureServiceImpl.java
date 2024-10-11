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
    public String addPropertyPictures(List<String> imgUrls, Long propertyId) {

        Properties currentProperty = propertiesService.getPropertiesById(propertyId);
        PropertyPicture lastSavedPicture = null;

        for (String imgUrl : imgUrls) {
            PropertyPicture propertyPicture = new PropertyPicture();
            propertyPicture.setImgUrl(imgUrl);
            propertyPicture.setProperties(currentProperty);
            propertyPictureRepository.save(propertyPicture);
        }

        return "success";
    }

    @Override
    public void deletePropertyPictures(List<Long> propertyPictureIds, String email) {
        for (Long propertyPictureId : propertyPictureIds) {
            Properties currentProperty = propertiesService.findPropertiesByPropertyPicture(propertyPictureId);
            if (currentProperty == null) {
                throw new DataNotFoundException("Property not found for picture ID: " + propertyPictureId);
            }

            PropertyPicture currentPropertyPicture = propertyPictureRepository.findById(propertyPictureId)
                    .orElseThrow(() -> new DataNotFoundException("Picture not found with ID: " + propertyPictureId));

            currentPropertyPicture.setDeletedAt(Instant.now());
            propertyPictureRepository.save(currentPropertyPicture);
        }
    }

    @Override
    public List<PropertyPicture> getPropertyPicturesbyPropertyId(Long userId) {
        return propertyPictureRepository.findPropertyPicturesByPropertiesId(userId);
    }
}
