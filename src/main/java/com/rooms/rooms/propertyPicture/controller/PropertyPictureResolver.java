package com.rooms.rooms.propertyPicture.controller;

import com.rooms.rooms.propertyPicture.entity.PropertyPicture;
import com.rooms.rooms.propertyPicture.service.PropertyPictureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PropertyPictureResolver {
    private final PropertyPictureService propertyPictureService;

    public PropertyPictureResolver(PropertyPictureService propertyPictureService) {
        this.propertyPictureService = propertyPictureService;
    }

    @MutationMapping(value = "addPropertyPictures")
    public String addPropertyPicture(@Argument Long propertyId, @Argument List<String> imgUrl) {
        return propertyPictureService.addPropertyPictures(imgUrl, propertyId);
    }

    @MutationMapping(value = "deletePropertyPictures")
    public String deletePropertyPictures(@Argument List<Long> propertyPictureId, @Argument String email) {
        propertyPictureService.deletePropertyPictures(propertyPictureId, email);
        return "Picture deleted";
    }
}
