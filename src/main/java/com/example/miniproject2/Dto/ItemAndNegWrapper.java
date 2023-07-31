package com.example.miniproject2.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemAndNegWrapper {
    private ItemDto itemDto;
    private NegotiationDto negotiationDto;
    private String writer;
    private String password;
    private String status;
    private int suggestedPrice;
}
