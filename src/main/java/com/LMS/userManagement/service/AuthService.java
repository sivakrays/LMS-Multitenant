package com.LMS.userManagement.service;

import com.LMS.userManagement.dto.AuthenticationResponse;
import com.LMS.userManagement.dto.LoginDTO;
import com.LMS.userManagement.dto.RegisterRequest;
import com.LMS.userManagement.dto.UserDto;
import com.LMS.userManagement.model.*;
import com.LMS.userManagement.repository.QuizRankRepository;
import com.LMS.userManagement.repository.UserRepository;
import com.LMS.userManagement.securityConfig.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    QuizRankRepository quizRankRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> register(RegisterRequest request) {

        User user=User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .confirmPassword(passwordEncoder.encode(request.getConfirmPassword()))
                .role(request.getRole().toLowerCase())
                .createdDate(new Timestamp(System.currentTimeMillis()))
                .build();
       var savedUser= userRepository.save(user);
       var userDto= UserDto.builder()
                .email(savedUser.getEmail())
                .createdDate(savedUser.createdDate)
                .role(savedUser.role)
                .name(savedUser.getName()).build();
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }



    public ResponseEntity<?> authentication(LoginDTO login, String tenantId) {
        String email= login.email();
        String password=login.password();
try {
    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    email,password
            )
    );
}catch (Exception e){
    return ResponseEntity.status(403).body("Bad Credential");
}

      var user =userRepository.findByEmail(email);
      long userId=user.getId();
        int goldCount ;
        int silverCount ;
        int bronzeCount;
        Integer energyPoints;
        try {
           goldCount = quizRankRepository.countByUserIdAndBadge(userId, 1);
           silverCount = quizRankRepository.countByUserIdAndBadge(userId, 2);
           bronzeCount = quizRankRepository.countByUserIdAndBadge(userId, 3);
           energyPoints = quizRankRepository.sumOfEnergyPoints(userId);
      }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
      }
        String jwtToken=jwtService.generateToken(user,tenantId);
       // revokeAllUserTokens(user);
       // saveUserToken(user, jwtToken);

       // String refreshToken=jwtService.generateRefreshToken(user,tenantId);
        var auth=  AuthenticationResponse.builder()
                .token(jwtToken)
                .role(user.getRole())
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .gold(goldCount)
                .silver(silverCount)
                .bronze(bronzeCount)
                .energyPoints(energyPoints)
             //   .refreshToken(refreshToken)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(auth);
    }


    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final String authHeader=request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            return;
        }

        refreshToken=authHeader.substring(7);
        userEmail=jwtService.extractUsername(refreshToken);
        String tenantId=   jwtService.extractTenantId(refreshToken);
        if(userEmail!=null && tenantId!=null){
            var user=this.userRepository.findByEmail(userEmail);
            if(jwtService.isTokenValid(refreshToken,user)){
                String accessToken=jwtService.generateToken(user,tenantId);
           //     revokeAllUserTokens(user);
            //    saveUserToken(user,accessToken);
               var authResponse= AuthenticationResponse.builder()
                        .token(accessToken)
                        //.refreshToken(refreshToken)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(),authResponse);
            }
        }

    }

    public ResponseEntity<?> getAllUser(int pageNo,int pageSize) {
        Pageable sortedByTime =
                PageRequest.of(pageNo, pageSize, Sort.by("createdDate").descending());
        Page<User> users=userRepository.findAll(sortedByTime);
        if(users.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(users);
        }
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    public ResponseEntity<?> deleteUserById(Long userId) {
        if (userRepository.existsById(userId)){
            userRepository.deleteById(userId);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        }
        return ResponseEntity.status(HttpStatus.OK).body("User not found");
    }
    /*private void saveUserToken(User user, String jwtToken) {
        var token=Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType("BEARER")
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user){
        var validUserTokens=tokenRepository.findAllvalidTokensByUser(user.getId());
        if(validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(t -> {
            t.setRevoked(true);
            t.setExpired(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }*/
}
