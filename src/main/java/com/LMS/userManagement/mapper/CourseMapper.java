package com.LMS.userManagement.mapper;

import com.LMS.userManagement.model.Course;
import com.LMS.userManagement.records.CourseDTO;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public CourseDTO CourseToCourseDtoMapper(Course course,String profileImage){
        return  new CourseDTO(
                course.getCourseId(),
                course.getUserId(),
                profileImage,
                course.getTitle(),
                course.getAuthorName(),
                course.getDescription(),
                course.getThumbNail(),
                course.getEnrolled(),
                course.getCategory(),
                course.getRatings(),
                course.getLanguage(),
                course.getOverview(),
                course.getWhatYouWillLearn(),
                course.getPrice(),
                course.getDate(),
                course.getSections()
        );
    }
}
