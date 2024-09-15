package com.rooms.rooms.propertyCategories.Service;

import com.rooms.rooms.propertyCategories.entity.PropertyCategories;

public interface PropertyCategoriesService {
    PropertyCategories getPropertyCategoriesByName (String categoryName);
}
