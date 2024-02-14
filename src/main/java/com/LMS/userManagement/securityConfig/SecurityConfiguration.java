package com.LMS.userManagement.securityConfig;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

   /* @Autowired
    @Qualifier("handlerExceptionResolver")
   private  HandlerExceptionResolver handlerExceptionResolver;*/

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final   AuthenticationProvider authenticationProvider;



    private static final String[] SWAGGER_AUTH_WHITELIST={
            "/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources",
            "/swagger-resources/**"
    };

  //  private final LogoutHandler logoutHandler;

 /*@Bean
 public JwtAuthenticationFilter jwtAuthFilter(){
        return new JwtAuthenticationFilter(handlerExceptionResolver);
    }*/
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.cors(AbstractHttpConfigurer::disable)
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/lms/api/auth/**").permitAll()
                                .requestMatchers(SWAGGER_AUTH_WHITELIST).permitAll()
                                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                                .requestMatchers("/lms/api/auth/refreshToken").permitAll()
                                .requestMatchers("/lms/api/tenant/**").permitAll()
                                .requestMatchers("/lms/api/admin/**").permitAll()
                                .requestMatchers("/lms/api/user/getCourseCompletion").permitAll()
                                .requestMatchers(HttpMethod.OPTIONS).permitAll()
                                /*.requestMatchers("/lms/api/auth/saveAndEditProfile").permitAll()
                                 .requestMatchers("/lms/api/user/saveSection").permitAll()
                                 .requestMatchers("/lms/api/user/saveCourse").permitAll()
                                 .requestMatchers("/lms/api/user/deleteCourseById").permitAll()
*/                                   /* .requestMatchers("/lms/api/user/").hasRole("user")
                                    .requestMatchers("/lms/api/admin").hasRole("admin")*/
                                .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        // .logout(logout -> logout.logoutUrl("lms/api/auth/logout")
        //         .addLogoutHandler(logoutHandler)
        //       .logoutSuccessHandler(((request, response, authentication) ->
        //            SecurityContextHolder.clearContext())))
        ;
        return http.build();

    }

   /* public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> {web.ignoring().requestMatchers("/v3/api-docs/**");
        };
    }*/
    }