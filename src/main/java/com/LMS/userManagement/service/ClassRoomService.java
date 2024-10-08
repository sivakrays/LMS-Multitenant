package com.LMS.userManagement.service;

import com.LMS.userManagement.dto.ClassRoomDashBoardDto;
import com.LMS.userManagement.dto.ClassRoomDto;
import com.LMS.userManagement.dto.ClassRoomNameDto;
import com.LMS.userManagement.dto.UserClassRoomDto;
import com.LMS.userManagement.model.ClassRoom;
import com.LMS.userManagement.model.ClassroomData;
import com.LMS.userManagement.model.User;
import com.LMS.userManagement.repository.ClassRoomRepository;
import com.LMS.userManagement.repository.ClassroomDataRepository;
import com.LMS.userManagement.repository.UserRepository;
import com.LMS.userManagement.response.CommonResponse;
import com.LMS.userManagement.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClassRoomService {

    @Autowired
    ClassRoomRepository classRoomRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    ClassroomDataRepository classroomDataRepository;


    public CommonResponse<List<ClassRoom>> createClassRoom(ClassRoomDto classRoomDto) {

        List<Long> userIds = classRoomDto.getUserIds();
        Long count = (long) userIds.size();
        ClassRoom classRoom = ClassRoom.builder()
                .classRoomName(classRoomDto.getClassRoomName())
              //  .userIds(classRoomDto.getUserIds())
                .createdBy(classRoomDto.getCreatedBy())
                .noOfUsers(count)
                .build();

        try {

            ClassRoom newClassRoom = classRoomRepository.save(classRoom);
            List<ClassroomData> classroomDataList=new ArrayList<>();
          String classId=  newClassRoom.getClassroomId();
            userIds.forEach(n-> {
                ClassroomData data=new ClassroomData();
                data.setClassroomId(classId);
                data.setUserId(n);
                classroomDataList.add(data);
            });
            classroomDataRepository.saveAll(classroomDataList);
            List<ClassRoom> classRoomList=classRoomRepository.findByCreatedBy(classRoom.getCreatedBy());
            return CommonResponse.<List<ClassRoom>>builder()
                    .statusCode(Constant.SUCCESS)
                    .status(true)
                    .data(classRoomList)
                    .message("ClassRoom Created Successfully !!")
                    .build();

        }catch (Exception e){

            List<ClassRoom> classRoomList=classRoomRepository.findByCreatedBy(classRoom.getCreatedBy());
            return CommonResponse.<List<ClassRoom>>builder()
                    .statusCode(Constant.FORBIDDEN)
                    .status(false)
                    .data(classRoomList)
                    .message("Unable to create the ClassRoom")
                    .build();

        }

    }

    public CommonResponse<List<ClassRoomDashBoardDto>> getAllClassRooms(long userId) {
        List<ClassRoomDashBoardDto> dashboardDtos = new ArrayList<>();

        try {
            List<ClassRoom> classRooms = classRoomRepository.findByCreatedBy(userId);

            for (ClassRoom classRoom : classRooms) {
                ClassRoomDashBoardDto dashBoardDto = new ClassRoomDashBoardDto();
                dashBoardDto.setClassRoomId(classRoom.getClassroomId());
                dashBoardDto.setClassRoomName(classRoom.getClassRoomName());
                dashBoardDto.setNoOfUsers(classRoom.getNoOfUsers());
                dashboardDtos.add(dashBoardDto);
            }

            return CommonResponse.<List<ClassRoomDashBoardDto>>builder()
                    .status(true)
                    .data(dashboardDtos)
                    .statusCode(Constant.SUCCESS)
                    .message("ClassRoom dashboard data fetched successfully")
                    .build();

        } catch (Exception e) {
            return CommonResponse.<List<ClassRoomDashBoardDto>>builder()
                    .status(false)
                    .data(new ArrayList<>())
                    .statusCode(Constant.FORBIDDEN)
                    .message("An error occurred while fetching ClassRoom dashboard data")
                    .build();
        }
    }


    public CommonResponse<List<UserClassRoomDto>> getAllUsers() {

        List<UserClassRoomDto> userDtos = new ArrayList<>();

        try {

            List<User> users = userRepository.findAll();

            for (User user : users) {
                UserClassRoomDto userDto = new UserClassRoomDto();
                userDto.setUserId(user.getId());
                userDto.setUserName(user.getName());
                userDtos.add(userDto);
            }

            return CommonResponse.<List<UserClassRoomDto>>builder()
                    .status(true)
                    .message("Users fetched successfully")
                    .data(userDtos)
                    .statusCode(Constant.SUCCESS)
                    .build();

        } catch (Exception e) {

            return CommonResponse.<List<UserClassRoomDto>>builder()
                    .status(false)
                    .message("An error occurred while fetching users")
                    .statusCode(Constant.FORBIDDEN)
                    .build();

        }
    }

    public CommonResponse<List<ClassRoom>> deleteClassRoom(String classRoomId,long userId) {

        Optional<ClassRoom> classRoom = classRoomRepository.findById(classRoomId);

        try {

            if (classRoom.isEmpty()){
                List<ClassRoom> classRoomList=classRoomRepository.findByCreatedBy(userId);
                return CommonResponse.<List<ClassRoom>>builder()
                        .status(true)
                        .message("There is no classroom")
                        .statusCode(Constant.NO_CONTENT)
                        .data(classRoomList)
                        .build();

            }

                classRoomRepository.deleteById(classRoomId);
                List<ClassRoom> classRoomList=classRoomRepository.findByCreatedBy(userId);
                return CommonResponse.<List<ClassRoom>>builder()
                        .status(true)
                        .statusCode(Constant.SUCCESS)
                        .message("Deleted Successfully")
                        .data(classRoomList)
                        .build();



        }catch (Exception e){

            List<ClassRoom> classRoomList=classRoomRepository.findByCreatedBy(userId);
            return CommonResponse.< List<ClassRoom>>builder()
                    .status(false)
                    .statusCode(Constant.FORBIDDEN)
                    .message("Something Went Wrong..")
                    .data(classRoomList)
                    .error(e.getMessage())
                    .build();

        }

    }

    public CommonResponse<List<ClassRoomNameDto>> getAllClassRoomNames(Long userId) {

        List<ClassRoomNameDto> names = new ArrayList<>();

        try {
            List<ClassRoom> classRooms = classRoomRepository.findByCreatedBy(userId);

            for (ClassRoom classRoom : classRooms) {
                ClassRoomNameDto nameDto = new ClassRoomNameDto();
                nameDto.setClassRoomName(classRoom.getClassRoomName());
                names.add(nameDto);
            }

            return CommonResponse.<List<ClassRoomNameDto>>builder()
                    .status(true)
                    .data(names)
                    .statusCode(Constant.SUCCESS)
                    .message("ClassRoom names fetched successfully")
                    .build();

        } catch (Exception e) {
            return CommonResponse.<List<ClassRoomNameDto>>builder()
                    .status(false)
                    .data(new ArrayList<>())
                    .statusCode(Constant.FORBIDDEN)
                    .message("An error occurred while fetching ClassRoom names")
                    .build();
        }
    }

}
