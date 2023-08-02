package com.example.miniproject2.Dto;

import com.example.miniproject2.Entity.Item;
import com.example.miniproject2.Entity.User;
import lombok.Data;

@Data
public class ItemDto {
    private Long id;
    private String title;
    private String description;
    private int minPriceWanted;
    private String writer;
    private String password;
    private String status;
    private String imageUrl;
    private User user;

    public static ItemDto fromEntity(Item entity) {
        ItemDto dto = new ItemDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setWriter(entity.getWriter());
        dto.setPassword(entity.getPassword());
        dto.setStatus(entity.getStatus());
        dto.setMinPriceWanted(entity.getMin_price_wanted());
        dto.setImageUrl(entity.getImage_url());
        dto.setUser(entity.getUser());

        return dto;
    }
}
