package com.rooms.rooms.propertyCategories.Service;

import com.rooms.rooms.propertyCategories.entity.PropertyCategories;

import java.util.List;

public interface PropertyCategoriesService {
    PropertyCategories getPropertyCategoriesByName (String categoryName);
    List<PropertyCategories> getAllPropertyCategories();
}
