package com.rooms.rooms.properties.service.impl;

import com.rooms.rooms.city.entity.City;
import com.rooms.rooms.city.service.impl.CityService;
import com.rooms.rooms.exceptions.DataNotFoundException;
import com.rooms.rooms.helper.SlugifyHelper;
import com.rooms.rooms.helper.StringGenerator;
import com.rooms.rooms.properties.dto.*;
import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.properties.repository.PropertiesRepository;
import com.rooms.rooms.properties.service.PropertiesService;
import com.rooms.rooms.propertyCategories.Service.PropertyCategoriesService;
import com.rooms.rooms.propertyCategories.entity.PropertyCategories;
import com.rooms.rooms.review.entity.Review;
import com.rooms.rooms.review.service.ReviewService;
import com.rooms.rooms.users.entity.Users;
import com.rooms.rooms.users.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class PropertiesServiceImpl implements PropertiesService {
    private final PropertiesRepository propertiesRepository;
    private final UsersService usersService;
    private final PropertyCategoriesService propertyCategoriesService;
    private final CityService cityService;
    private final ReviewService reviewService;


    public PropertiesServiceImpl(PropertiesRepository propertiesRepository, UsersService usersService, PropertyCategoriesService propertyCategoriesService, CityService cityService, @Lazy ReviewService reviewService) {
        this.propertiesRepository = propertiesRepository;
        this.usersService = usersService;
        this.propertyCategoriesService = propertyCategoriesService;
        this.cityService = cityService;
        this.reviewService = reviewService;
    }

    @Override
    public List<Properties> getAllProperties() {
        return propertiesRepository.findAll();
    }

    @Override
//    @Cacheable(value = "getPropertiesById", key = "#id")
    public Properties getPropertiesById(Long id) {
        return propertiesRepository.findById(id).orElseThrow(() -> new DataNotFoundException("No properties found with id: " + id));
    }

    @Override
    public List<Properties> getPropertiesByOwnerEmail(String ownerEmail) {
        System.out.println("KOCAKKKKKKKK");
        System.out.println(ownerEmail);
        return propertiesRepository.findByUserEmail(ownerEmail).orElseThrow(()-> new DataNotFoundException("No properties found with ownerEmail: " + ownerEmail));
    }

    @Override
    public Properties getPropertiesBySlug(String slug) {
        return propertiesRepository.findPropertiesBySlug(slug);
    }

    @Override
    public PagedPropertyResult getAllPropertyProjections(Double rating, Double startPrice, Double endPrice, Boolean isBreakfast, String city, Integer page, String category, String sortBy) {
        System.out.println("Rating: " + rating);
        System.out.println("Start Price: " + startPrice);
        System.out.println("End Price: " + endPrice);
        Sort sort = createSort(sortBy);
        int pageNumber = page ;
        Pageable pageable = PageRequest.of(pageNumber, 5, sort);
        Page<PropertyProjection> result = propertiesRepository.findFilteredPropertiesWithPrice(
                rating, startPrice, endPrice, isBreakfast, city, category, pageable);

        PagedPropertyResult pagedPropertyResult = new PagedPropertyResult();

        return pagedPropertyResult.toDto(result, pagedPropertyResult);
    }

    private Sort createSort(String sortBy) {
        if (sortBy == null) {
            return Sort.unsorted();
        }
        return switch (sortBy) {
            case "price_asc" -> Sort.by("price").ascending();
            case "price_desc" -> Sort.by("price").descending();
            case "rating_desc" -> Sort.by("averageRating").descending();
            default -> Sort.unsorted();
        };
    }

    @Transactional
    @Override
    public Properties createProperties(CreatePropertyRequestDto dto) {
        Users user = usersService.findByEmail(dto.getEmail());
        PropertyCategories category = propertyCategoriesService.getPropertyCategoriesByName(dto.getPropertyCategories());
        City city = cityService.findACity(dto.getCity());
        String slug = SlugifyHelper.slugify((dto.getPropertyName()));
        String uniqueCode = StringGenerator.generateRandomString(4);
        Properties newProperties = new Properties();

        newProperties.setUsers(user);
        newProperties.setPropertyCategories(category);
        newProperties.setCity(city);
        newProperties.setAddress(dto.getAddress());
        newProperties.setDescription(dto.getDescription());
        newProperties.setCheckInTime(dto.getCheckInTime());
        newProperties.setCheckOutTime(dto.getCheckOutTime());
        newProperties.setStar(dto.getStar());
        newProperties.setPhoneNumber(dto.getPhoneNumber());
        newProperties.setName(dto.getPropertyName());
        newProperties.setSlug(slug + "-" + uniqueCode);
        return propertiesRepository.save(newProperties);
    }

    @Transactional
    @Override
    public Properties updateProperties(Long propertyId, UpdatePropertyRequestDto dto) {
        Properties currentProperty = propertiesRepository.findById(propertyId).orElseThrow(() -> new DataNotFoundException("Properties with id " + propertyId + " not found"));
        City city = cityService.findACity(dto.getCity());
        PropertyCategories category = propertyCategoriesService.getPropertyCategoriesByName(dto.getPropertyCategories());
        dto.toEntity(currentProperty, city, category);
        propertiesRepository.save(currentProperty);
        return currentProperty;
    }

    @Override
    public void addSlug() {
        List<Properties> allProperties = propertiesRepository.findAll();
        for (Properties properties : allProperties) {
            String name = properties.getName();
            String slug = SlugifyHelper.slugify(name);
            String uniqueCode = StringGenerator.generateRandomString(4);
            properties.setSlug(slug + "-" + uniqueCode);
            Properties savedProperties = propertiesRepository.save(properties);
        }
    }

    @Transactional
    @Override
    public void deleteProperties(Long id) {
        Properties currentProperty = propertiesRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Properties with id " + id + " not found"));
        currentProperty.setDeletedAt(Instant.now());
        propertiesRepository.save(currentProperty);
    }

    @Override
    public Properties findPropertiesByPropertyPicture(Long propertiesId) {
        return propertiesRepository.findPropertiesByPropertyPictureIds(propertiesId);
    }

    @Override
    @Transactional(readOnly = true)
    public PropertyOwnerDto getPropertyOwnerById(Long id) {
        Properties property = propertiesRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Property not found"));
        PropertyOwnerDto dto = new PropertyOwnerDto();
        dto.setId(property.getId());
        dto.setEmail(property.getUsers().getEmail());
        return dto;
    }


    @Transactional
    @Scheduled(cron = "0 0 1 * * ?")
    public void updateTotalReviews() {
        propertiesRepository.updateAllPropertiesTotalReviews();
    }


    @Transactional
    @Scheduled(cron = "0 5 1 * * ?")
    public void updateAverageRating() {
        List<Properties> properties = propertiesRepository.findAll();
        for (Properties property : properties) {
            List<Review> reviews = reviewService.findReviewbyProperties(property);
            if (!reviews.isEmpty()) {
                double averageRating = reviews.stream()
                        .mapToInt(Review::getRating)
                        .average()
                        .orElse(0.0);
                property.setAverageRating(averageRating);
                propertiesRepository.save(property);
            }
        }
    }
}
