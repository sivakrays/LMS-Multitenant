package com.LMS.userManagement.service;

import com.LMS.userManagement.dto.AdminDto;
import com.LMS.userManagement.dto.LoginDto;
import com.LMS.userManagement.model.Admin;
import com.LMS.userManagement.model.TenantDetails;
import com.LMS.userManagement.repository.AdminRepository;
import com.LMS.userManagement.repository.TenantRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    private final TenantRepository tenantRepository;

    private final TenantService tenantService;

    @PersistenceContext
    private EntityManager entityManager;


    public ResponseEntity<?> adminRegistration(AdminDto adminDto) {
        var adminDetails = adminRepository.findAllByEmail(adminDto.getEmail());
        if (adminDetails.isPresent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User already exists");

        }
        var admin = Admin.builder()
                .role("owner")
                .password(adminDto.getPassword())
                .createdDate(new Timestamp(System.currentTimeMillis()))
                .email(adminDto.getEmail())
                .build();
        var savedAdmin = adminRepository.save(admin);
        return ResponseEntity.status(HttpStatus.OK).body(savedAdmin);
    }

    public ResponseEntity<?> adminLogin(LoginDto login) {
        String email=login.email();
        String password= login.password();
        Optional<Admin> admin = adminRepository.findAllByEmail(email);
        if (admin.isPresent() && admin.get().getPassword().equals(password)) {
            var ad = admin.get();
            var adminDto = AdminDto.builder()
                    .password(null)
                    .email(ad.getEmail())
                    .role(ad.getRole())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(adminDto);
        }
        return ResponseEntity.status(403).body("User not found");
    }

    @Transactional
    public ResponseEntity<?> deleteTenant(long id) {
        Optional<TenantDetails> tenant = tenantRepository.findById(id);
        if (tenant.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("User Not found");
        }
        var tenantDtls = tenant.get();
        String schemaName = tenantDtls.getTenantId();
        entityManager.createNativeQuery("DROP SCHEMA IF EXISTS " + schemaName + " CASCADE").executeUpdate();
        tenantRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("User removed successfully");
    }


    public ResponseEntity<?> getAllTenants() {
        List<TenantDetails> tenantList = tenantRepository.findAll();
        if (tenantList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(tenantList);
        }
        Map<String, String> tenantIdMap = new HashMap<>();
        tenantList.forEach(n -> {
            tenantIdMap.put(n.getIssuer(), n.getTenantId());
        });

        return ResponseEntity.status(HttpStatus.OK).body(tenantIdMap);
    }


    public ResponseEntity<?> findAllTenants(int pageNo,int pageSize) {
        Page<TenantDetails> tenantList = tenantRepository.findAll(PageRequest.of(pageNo, pageSize));
        if (tenantList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(tenantList);
        }
        return ResponseEntity.status(HttpStatus.OK).body(tenantList);
    }

    @Transactional
    public ResponseEntity<?> updateSchemaByTenant(String email) {
        Optional<TenantDetails> tenantDetails =
                tenantRepository.findByEmail(email);
        if (tenantDetails.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(tenantDetails);
        }
        tenantService.initDatabase(tenantDetails.get().getTenantId());
        return ResponseEntity.status(HttpStatus.OK).body("Schema updated successfully");
    }
}
