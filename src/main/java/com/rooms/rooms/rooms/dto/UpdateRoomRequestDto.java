package com.rooms.rooms.rooms.dto;

import com.rooms.rooms.bedTypes.entity.BedTypes;
import com.rooms.rooms.helper.SlugifyHelper;
import com.rooms.rooms.helper.StringGenerator;
import com.rooms.rooms.rooms.entity.Rooms;
import lombok.Data;

@Data
public class UpdateRoomRequestDto {
    private String name;
    private String description;
    private Integer capacity;
    private String roomNumber;
    private Double price;
    private Boolean includeBreakfast;
    private String bedType;
    private Integer roomArea;

    public Rooms toEntity(Rooms room, BedTypes bedType) {
        if (this.name != null && !this.name.trim().isEmpty()) {
            room.setName(this.name);
            String slug = SlugifyHelper.slugify((room.getName()));
            String uniqueCode = StringGenerator.generateRandomString(4);
            room.setSlug(slug+"-"+uniqueCode);
        }
        if (this.description != null && !this.description.trim().isEmpty()) {
            room.setDescription(this.description);
        }
        if (this.capacity != null) {
            room.setCapacity(this.capacity);
        }
        if (this.roomNumber != null && !this.roomNumber.trim().isEmpty()) {
            room.setRoomNumber(this.roomNumber);
        }
        if (this.price != null) {
            room.setPrice(this.price);
        }
        if (this.includeBreakfast != null) {
            room.setIncludeBreakfast(this.includeBreakfast);
        }
        if (bedType != null) {
            room.setBedTypes(bedType);
        }
        if (this.roomArea != null) {
            room.setRoomArea(this.roomArea);
        }

        return room;
    }
}
