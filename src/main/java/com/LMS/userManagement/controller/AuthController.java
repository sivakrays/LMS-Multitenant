package com.LMS.userManagement.controller;

import com.LMS.userManagement.dto.RegisterRequest;
import com.LMS.userManagement.model.User;
import com.LMS.userManagement.records.LoginResponse;
import com.LMS.userManagement.records.UserDTO;
import com.LMS.userManagement.response.CommonResponse;
import com.LMS.userManagement.records.LoginDTO;
import com.LMS.userManagement.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/lms/api/auth")
@CrossOrigin(origins = "*",allowedHeaders = "*")
@RequiredArgsConstructor
@Tag(name = "User", description = "User management APIs")

public class AuthController {

    @Autowired
    private  AuthService authService;


    @PostMapping("/register")
    //  @PreAuthorize("hasAuthority('manager')")
    public CommonResponse<UserDTO> register (@RequestBody RegisterRequest request){
        return authService.register(request);

    }

    @PostMapping("/login")
    // @PreAuthorize("hasAuthority('user')")
    public CommonResponse<LoginResponse> authentication (
            @RequestBody LoginDTO loginDto,
            @RequestHeader String tenantId) {
        return authService.authentication(loginDto,tenantId);

    }


    @PostMapping("/refreshToken")
    public void refreshToken(HttpServletRequest request,
                             HttpServletResponse response) throws IOException {
        authService.refreshToken(request,response);
    }

    @GetMapping("/getAllUser")
    private CommonResponse<Page<User>> getAllUser(@RequestParam int pageNo,
                                                  @RequestParam int pageSize){

        return   authService.getAllUser(pageNo,pageSize);
    }
    @DeleteMapping("/deleteUserById")
    public CommonResponse<Page<User>> deleteUserById(@RequestParam Long userId,@RequestParam int pageNo,
                                            @RequestParam int pageSize){
        return authService.deleteUserById(userId,pageNo,pageSize);
    }


}