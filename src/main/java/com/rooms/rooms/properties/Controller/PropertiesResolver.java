package com.rooms.rooms.properties.Controller;

import com.rooms.rooms.properties.dto.CreatePropertyRequestDto;
import com.rooms.rooms.properties.dto.GetPropertyResponseDto;
import com.rooms.rooms.properties.dto.UpdatePropertyRequestDto;
import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.properties.service.PropertiesService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PropertiesResolver {
    private PropertiesService propertiesService;

    public PropertiesResolver(PropertiesService propertiesService) {
        this.propertiesService = propertiesService;
    }

    @QueryMapping(value = "getPropertyById")
    public GetPropertyResponseDto getPropertiesById(@Argument Long id) {
        return propertiesService.getPropertyById(id);
    }

    @QueryMapping(value = "getPropertyByName")
    public GetPropertyResponseDto getPropertiesByName(@Argument String name) {
        return propertiesService.getPropertiesByName(name);
    }

    @QueryMapping (value = "getAllProperties")
    public List<Properties> getAllProperties() {
        return propertiesService.getAllProperties();
    }


    @MutationMapping(value = "createProperties")
    public String createProperties(@Argument("input") CreatePropertyRequestDto dto) {
        propertiesService.createProperties(dto);
        return "success";
    }

    @MutationMapping(value = "updateProperties")
    public String updateProperties(@Argument Long id, @Argument("input") UpdatePropertyRequestDto dto) {
        propertiesService.updateProperties(id, dto);
        return "success";
    }

    @MutationMapping(value = "deleteProperties")
    public String deleteProperties(@Argument Long id) {
        propertiesService.deleteProperties(id);
        return "Your property has been deleted";
    }
}
