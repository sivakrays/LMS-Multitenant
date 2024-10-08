package com.LMS.userManagement.controller;

import com.LMS.userManagement.dto.ProfileDto;
import com.LMS.userManagement.dto.UserProfileDto;
import com.LMS.userManagement.model.User;
import com.LMS.userManagement.response.CommonResponse;
import com.LMS.userManagement.service.ProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lms/api/user")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "UserProfile", description = "UserProfile management APIs")
public class ProfileController {

    @Autowired
    ProfileService profileService;

    @PutMapping(value = "/saveAndEditProfile")
//    public CommonResponse<UserProfileDto> saveAndEditProfile(@RequestPart(value = "profile") ProfileDto profileRequest, @RequestPart(value = "file",required = false) MultipartFile file){
    public CommonResponse<UserProfileDto> saveAndEditProfile(@RequestBody ProfileDto profileRequest) {
        return profileService.saveAndEditProfile(profileRequest);
    }

    @GetMapping("/getProfileById")
    public CommonResponse<User> getProfileById(@RequestParam Long id) {
        return profileService.getProfileById(id);
    }

}