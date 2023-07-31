package com.example.miniproject2.Dto;

import com.example.miniproject2.Entity.Negotiation;
import lombok.Data;

@Data
public class NegotiationDto {
    private Long id;
    private Long itemId;
    private String writer;
    private int suggestedPrice;
    private String password;
    private String status;

    public static NegotiationDto fromEntity(Negotiation entity) {
        NegotiationDto dto = new NegotiationDto();
        dto.setId(entity.getId());
        dto.setItemId(entity.getItemId());
        dto.setWriter(entity.getWriter());
        dto.setPassword(entity.getPassword());
        dto.setSuggestedPrice(entity.getSuggestedPrice());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}
