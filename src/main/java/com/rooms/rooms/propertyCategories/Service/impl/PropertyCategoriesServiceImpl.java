package com.rooms.rooms.propertyCategories.Service.impl;

import com.rooms.rooms.exceptions.DataNotFoundException;
import com.rooms.rooms.propertyCategories.Repository.PropertyCategoryRepository;
import com.rooms.rooms.propertyCategories.Service.PropertyCategoriesService;
import com.rooms.rooms.propertyCategories.entity.PropertyCategories;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyCategoriesServiceImpl implements PropertyCategoriesService {
    private PropertyCategoryRepository propertyCategoryRepository;

    public PropertyCategoriesServiceImpl(PropertyCategoryRepository propertyCategoryRepository) {
        this.propertyCategoryRepository = propertyCategoryRepository;
    }

    @Override
    public PropertyCategories getPropertyCategoriesByName(String categoryName) {
        return propertyCategoryRepository.findByName(categoryName).orElseThrow(()->new DataNotFoundException("Categories not found"));
    }

    @Override
    public List<PropertyCategories> getAllPropertyCategories() {
        return propertyCategoryRepository.findAll();
    }
}
