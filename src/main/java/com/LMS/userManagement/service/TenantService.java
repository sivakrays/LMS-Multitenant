package com.LMS.userManagement.service;

import com.LMS.userManagement.dto.LoginDTO;
import com.LMS.userManagement.dto.TenantDto;
import com.LMS.userManagement.model.TenantDetails;
import com.LMS.userManagement.repository.TenantRepository;
import jakarta.transaction.Transactional;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.Optional;

@Service
public class TenantService {

    @Autowired
    TenantRepository tenantRepository;

    @Autowired
    AuthService authService;

    @Autowired
    DataSource dataSource;

    public void initDatabase(String schema) {
        Flyway flyway = Flyway.configure()
                .locations("db/migration/tenants")
                .baselineVersion("0")
                .dataSource(dataSource)
                .schemas(schema)
                .load();
        flyway.migrate();
    }

    @Transactional
    public ResponseEntity<?> registerTenant(TenantDto tenantDetails) {
        Optional<TenantDetails> tenant=tenantRepository.findByTenantId(tenantDetails.getTenantId());
        if(tenant.isEmpty()){
      var tenantDtls=      TenantDetails.builder()
                    .tenantId(tenantDetails.getTenantId())
                    .role("manager")
                    .issuer(tenantDetails.getIssuer())
                    .email(tenantDetails.getEmail())
                    .password(tenantDetails.getPassword())
              .createdDate(new Timestamp(System.currentTimeMillis())).build();
             var savedTenant=   tenantRepository.save(tenantDtls);
          if (savedTenant!=null){
              initDatabase(tenantDetails.getTenantId());
           var t=   TenantDto.builder()
                      .role(savedTenant.getRole())
                      .issuer(savedTenant.getIssuer())
                      .tenantId(savedTenant.getTenantId())
                      .email(savedTenant.getEmail()).build();

              return ResponseEntity.status(HttpStatus.OK).body(t);

          }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Tenant already exists");
    }

    public ResponseEntity<?> tenantLogin(LoginDTO login) {
        String email=login.email();
        String password=login.password();
        Optional<TenantDetails> tenant=tenantRepository.findByEmail(email);
        if (tenant.isPresent() && tenant.get().getPassword().equals(password)){
          var t=  tenant.get();
         var tenantDto =  TenantDto.builder()
                    .email(t.getEmail())
                    .role(t.getRole())
                    .issuer(t.getIssuer())
                    .tenantId(t.getTenantId())
                    .build();


           return ResponseEntity.status(HttpStatus.OK).body(tenantDto);
        }
        return ResponseEntity.status(403).body("user not found");
    }



}
