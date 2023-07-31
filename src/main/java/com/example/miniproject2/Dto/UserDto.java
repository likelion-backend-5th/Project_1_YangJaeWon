package com.example.miniproject2.Dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;

}
