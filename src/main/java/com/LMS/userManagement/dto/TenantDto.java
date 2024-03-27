package com.LMS.userManagement.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TenantDto {

    private String email;

    private String password;

    private String tenantId;

    private String issuer;

    private String role;


}
