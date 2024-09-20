package com.rooms.rooms.propertyPicture.controller;

import com.rooms.rooms.propertyPicture.entity.PropertyPicture;
import com.rooms.rooms.propertyPicture.service.PropertyPictureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class PropertyPictureResolver {
    private final PropertyPictureService propertyPictureService;

    public PropertyPictureResolver(PropertyPictureService propertyPictureService) {
        this.propertyPictureService = propertyPictureService;
    }

    @MutationMapping(value = "addPropertyPicture")
    public PropertyPicture addPropertyPicture(@Argument Long propertyId, @Argument String imgUrl) {
        return propertyPictureService.addPropertyPicture(imgUrl, propertyId);
    }

    @MutationMapping(value = "deletePropertyPicture")
    public String deletePropertyPicture(@Argument Long propertyPictureId, @Argument String email) {
        propertyPictureService.deletePropertyPicture(propertyPictureId, email);
        return "Picture deleted";
    }
}
