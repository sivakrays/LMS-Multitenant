package com.LMS.userManagement.controller;

import com.LMS.userManagement.dto.ProfileDto;
import com.LMS.userManagement.model.User;
import com.LMS.userManagement.response.CommonResponse;
import com.LMS.userManagement.service.ProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/lms/api/user")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "UserProfile", description = "UserProfile management APIs")

public class ProfileController {
    @Autowired
    ProfileService profileService;
    @PostMapping("/saveAndEditProfile")
    public CommonResponse<User> saveAndEditProfile(@RequestBody ProfileDto profileRequest){
        return profileService.saveAndEditProfile(profileRequest);
    }
    @GetMapping("/getProfileById")
    public CommonResponse<User> getProfileById(@RequestParam Long id){
        return profileService.getProfileById(id);
    }

}