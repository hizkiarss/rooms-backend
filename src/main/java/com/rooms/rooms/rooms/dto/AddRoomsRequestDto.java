package com.rooms.rooms.rooms.dto;

import com.rooms.rooms.bedTypes.entity.BedTypes;
import com.rooms.rooms.helper.SlugifyHelper;
import com.rooms.rooms.helper.StringGenerator;
import com.rooms.rooms.properties.entity.Properties;
import com.rooms.rooms.rooms.entity.Rooms;
import lombok.Data;


@Data
public class AddRoomsRequestDto {
    private String name;
    private String description;
    private Integer capacity;
    private Double price;
    private Long propertyId;
    private Boolean includeBreakfast;
    private String bedType;
    private Integer roomArea;
    private Integer numberOfRooms;


    public Rooms toEntity(Properties property, BedTypes bedType) {
        Rooms room = new Rooms();
        String slug = SlugifyHelper.slugify(this.name);
        String uniqueCode = StringGenerator.generateRandomString(4);
        room.setName(this.name);
        room.setDescription(this.description);
        room.setCapacity(this.capacity);
        room.setPrice(this.price);
        room.setProperties(property);
        room.setIncludeBreakfast(this.includeBreakfast);
        room.setBedTypes(bedType);
        room.setIsAvailable(true);
        room.setRoomArea(this.roomArea);
        room.setSlug(slug+"-"+uniqueCode);
        return room;
    }
}
