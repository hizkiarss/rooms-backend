package com.rooms.rooms.propertyCategories.Controller;

import com.rooms.rooms.propertyCategories.Service.PropertyCategoriesService;
import com.rooms.rooms.propertyCategories.entity.PropertyCategories;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Properties;

@Controller
public class PropertyCategoriesResolver {
    private final PropertyCategoriesService propertyCategoriesService;

    public PropertyCategoriesResolver(PropertyCategoriesService propertyCategoriesService) {
        this.propertyCategoriesService = propertyCategoriesService;
    }
    @QueryMapping(value = "getAllPropertyCategories")
    public List<PropertyCategories> getAllPropertyCategories() {
        return propertyCategoriesService.getAllPropertyCategories();
    }
}
