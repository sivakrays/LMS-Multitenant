package com.LMS.userManagement.model;

import com.LMS.userManagement.util.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "userDetails")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false,unique = true)
   // @Email(message = "Invalid email Format")
    public String email;

    @Column(nullable = false)
    @JsonIgnore
    public String password;

    @Column(nullable = false)
    @JsonIgnore
    public String confirmPassword;

    public Timestamp createdDate;

    @Column(columnDefinition = "VARCHAR(255) default ''")
    public String role;

    @Column(columnDefinition = "VARCHAR(255) default ''")
    public String gender;

    @Column(columnDefinition = "VARCHAR(255) default ''")
  //  @ColumnDefault(value = "' '")
    public String school;

    @Column(columnDefinition = "VARCHAR(255) default ''")
    public Integer standard;

    @Column(columnDefinition = "VARCHAR(255) default ''")
    public String city;

    @Column(columnDefinition = "VARCHAR(255) default ''")
    public String country;

    @Column(columnDefinition = "TEXT default ''")
    public String profileImage;

  /* @OneToMany(fetch = FetchType.LAZY,mappedBy = "user",cascade = CascadeType.ALL)
    private List<Token> tokens;*/

    @Override
  //  @JsonView(Views.MyResponseViews.class)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
  //  @JsonView(Views.MyResponseViews.class)
    public String getUsername() {
        return email;
    }

    @Override
   // @JsonView(Views.MyResponseViews.class)
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
 //   @JsonView(Views.MyResponseViews.class)
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
 //   @JsonView(Views.MyResponseViews.class)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
   // @JsonView(Views.MyResponseViews.class)
    public boolean isEnabled() {
        return true;
    }
}
