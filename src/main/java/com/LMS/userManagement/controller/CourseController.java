package com.LMS.userManagement.controller;

import com.LMS.userManagement.dto.CourseDetailDto;
import com.LMS.userManagement.model.Course;
import com.LMS.userManagement.model.Quiz;
import com.LMS.userManagement.model.Section;
import com.LMS.userManagement.model.SubSection;
import com.LMS.userManagement.records.CourseDTO;
import com.LMS.userManagement.response.CommonResponse;
import com.LMS.userManagement.service.CourseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/lms/api/user")
@CrossOrigin(origins = "*",allowedHeaders = "*")
@Tag(name = "Course", description = "Course management APIs")

public class CourseController {
    @Autowired
    CourseService courseService;
    @PostMapping("/saveCourse")
  //  @PreAuthorize("hasAuthority('admin')")
    public CommonResponse<Course> saveCourse(@RequestBody Course course){
     return courseService.saveCourse(course);

    }
    @GetMapping("/getCourseById")
   // @PreAuthorize("hasAuthority('user') or hasAuthority('admin')")
    public CommonResponse<CourseDTO> getCourseById(@RequestParam UUID courseId){
    return courseService.getCourseById(courseId);

    }
   @GetMapping("/getAllCourse")
 //  @PreAuthorize("hasAuthority('user') or hasAuthority('admin')")
    public CommonResponse<List<CourseDetailDto>> getAllCourses() throws InterruptedException {
        return courseService.getAllCourses();

    }
    @GetMapping("/searchCourses")
    public CommonResponse<List<CourseDetailDto> > searchCourses(@RequestParam("search") String search){
        return courseService.searchCourses(search);

    }
    @PostMapping("/saveSection")
   // @PreAuthorize("hasAuthority('admin')")
    public CommonResponse<List<Section>>saveSection(@RequestBody List<Section> sections){
        return courseService.saveSection(sections);
    }
    @DeleteMapping("/deleteCourseById")
  //  @PreAuthorize("hasAuthority('admin')")
    public CommonResponse< Page<Course>> deleteCourseById(@RequestParam UUID courseId,@RequestParam int pageNo,@RequestParam int pageSize){
        return courseService.deleteCourseById(courseId,pageNo,pageSize);
    }
    @PutMapping("/updateCourse")
   // @PreAuthorize("hasAuthority('admin')")
    public  CommonResponse<Course> updateCourse(@RequestBody Course course){
        return courseService.updateCourse(course);
    }
    @PutMapping("/updateSection")
   // @PreAuthorize("hasAuthority('admin')")
    public  CommonResponse<Section>updateSection(@RequestBody Section section){
        return courseService.updateSection(section);
    }
    @PutMapping("/updateSubSection")
   // @PreAuthorize("hasAuthority('admin')")
    public  CommonResponse<SubSection> updateSubSection(@RequestBody SubSection subSection){
        return courseService.updateSubSection(subSection);
    }
    @PutMapping("/updateQuiz")
 //   @PreAuthorize("hasAuthority('admin')")
    public  CommonResponse<Quiz> updateQuiz(@RequestBody Quiz quiz){
        return courseService.updateQuiz(quiz);
    }
    @GetMapping("/getCourseByUserId")
    public CommonResponse<Page<Course>> getCourseByUserId(@RequestParam Long userId,@RequestParam int pageNo,@RequestParam int pageSize){
        return courseService.getCourseByUserId(userId,pageNo,pageSize);
    }

   /* @GetMapping("/getCourseCompletion")
    public ResponseEntity<?> getCourseCompletion(@RequestHeader int courseId){
        return courseService.getCourseCompletion(courseId);
    }*/
}

