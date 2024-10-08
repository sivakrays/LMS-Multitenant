package com.LMS.userManagement.controller;

import com.LMS.userManagement.dto.CourseDetailDto;
import com.LMS.userManagement.dto.PurchasedCourseDto;
import com.LMS.userManagement.model.PurchasedCourse;
import com.LMS.userManagement.response.CommonResponse;
import com.LMS.userManagement.service.PurchasedCourseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("lms/api/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Purchased Course", description = "Purchased Course APIs")
public class PurchasedCourseController {

    @Autowired
    private PurchasedCourseService purchasedCourseService;

    @PostMapping("/savePurchasedCourse")
    public CommonResponse<List<PurchasedCourse>> savePurchasedCourse(@RequestBody PurchasedCourseDto purchasedCourseDto) {
        return purchasedCourseService.savePurchasedCourse(purchasedCourseDto);
    }

    @GetMapping("/getPurchasedCourse")
    public CommonResponse<List<CourseDetailDto>> getPurchasedCourseByUserId(@RequestParam Long userId) {
        return purchasedCourseService.getPurchasedCoursesByUserId(userId);
    }

    @DeleteMapping("/deletePurchasedCourse")
    public CommonResponse<String> deletePurchased(@RequestParam long id) {
        return purchasedCourseService.deletePurchased(id);
    }

}