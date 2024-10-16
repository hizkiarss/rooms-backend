package com.rooms.rooms.properties.dto;

import com.rooms.rooms.city.entity.City;
import com.rooms.rooms.helper.SlugifyHelper;
import com.rooms.rooms.helper.StringGenerator;
import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.propertyCategories.entity.PropertyCategories;
import lombok.Data;

import java.time.LocalTime;

@Data
public class UpdatePropertyRequestDto {
    private String propertyName;
    private String propertyCategories;
    private String description;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private String address;
    private String city;
    private String phoneNumber;
    private int star;

    public void toEntity(Properties property, City city, PropertyCategories category) {
        String uniqueCode = StringGenerator.generateRandomString(4);
        String slug = SlugifyHelper.slugify(this.propertyName);

        if (this.propertyName != null) {
            property.setName(this.propertyName);
        }
        if (this.description != null) {
            property.setDescription(this.description);
        }
        if (this.address != null) {
            property.setAddress(this.address);
        }
        if (this.checkInTime != null) {
            property.setCheckInTime(this.checkInTime);
        }
        if (this.checkOutTime != null) {
            property.setCheckOutTime(this.checkOutTime);
        }
        if (city != null) {
            property.setCity(city);
        }
        if (category != null) {
            property.setPropertyCategories(category);
        }
        if (this.propertyName != null) {
            property.setSlug(slug + "-" + uniqueCode);
        }
        if (this.phoneNumber != null) {
            property.setPhoneNumber(this.phoneNumber);
        }
        property.setStar(this.star);
    }
}
