package com.LMS.userManagement.controller;

import com.LMS.userManagement.dto.LoginDTO;
import com.LMS.userManagement.dto.RegisterRequest;
import com.LMS.userManagement.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/lms/api/auth")
@CrossOrigin(origins = "*",allowedHeaders = "*")
@RequiredArgsConstructor
public class AuthController {


    @Autowired
    private  AuthService authService;




    @PostMapping("/register")
  //  @PreAuthorize("hasAuthority('manager')")
    public ResponseEntity<?> register (
                            @RequestBody RegisterRequest request){
try {
    return authService.register(request);
}catch (Exception e){
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User already exists");
}
}

    @PostMapping("/login")
   // @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<?> authentication (@RequestBody LoginDTO login,
                                             @RequestHeader String tenantId) {
        return authService.authentication(login,tenantId);

    }

  
    @PostMapping("/refreshToken")
    public void refreshToken(HttpServletRequest request,
                            HttpServletResponse response) throws IOException {
        authService.refreshToken(request,response);
    }

    @GetMapping("/getAllUser")
    //@JsonView(Views.MyResponseViews.class)
    private ResponseEntity<?> getAllUser(@RequestHeader int pageNo,
                                         @RequestHeader int pageSize){
      return   authService.getAllUser(pageNo,pageSize);
    }
    @DeleteMapping("/deleteUserById")
    public ResponseEntity<?> deleteUserById(@RequestHeader Long userId){
        return authService.deleteUserById(userId);
    }


}
