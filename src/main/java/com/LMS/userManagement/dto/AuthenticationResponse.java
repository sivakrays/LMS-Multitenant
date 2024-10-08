package com.LMS.userManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String token;

    private String role;

    private Long userId;

    private String name;

    private String email;

    private int gold;

    private int silver;

    private int bronze;

    private Integer energyPoints;

    private String profileImage;

    private int cartCount;

    private String school;

    private String gender;

    private Integer standard;

    private String city;

    private String country;

    //   private String refreshToken;

}
