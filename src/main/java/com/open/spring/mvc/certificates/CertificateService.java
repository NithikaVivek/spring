package com.open.spring.mvc.certificates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class CertificateService {
    
    @Autowired
    private CertificateRepository repository;
    
    private static final List<String> REQUIRED_MODULES = Arrays.asList(
        "submodule-1", "submodule-2", "submodule-3", "submodule-4"
    );
    
    public CertificateDTO.Response issueCertificate(CertificateDTO.Request request) {
        // Validate
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            return buildErrorResponse("Name is required");
        }
        
        if (request.getEmail() == null || !isValidEmail(request.getEmail())) {
            return buildErrorResponse("Valid email is required");
        }
        
        if (repository.existsByEmail(request.getEmail())) {
            return buildErrorResponse("Certificate already issued for this email");
        }
        
        if (request.getModulesCompleted() == null || 
            !request.getModulesCompleted().containsAll(REQUIRED_MODULES)) {
            return buildErrorResponse("All 4 submodules must be completed");
        }
        
        // Generate certificate
        Certificate cert = new Certificate();
        cert.setId(generateCertificateId());
        cert.setStudentName(request.getName());
        cert.setEmail(request.getEmail());
        cert.setIssueDate(LocalDateTime.now());
        cert.setModulesCompleted(request.getModulesCompleted());
        cert.setStatus(Certificate.Status.ACTIVE);
        
        repository.save(cert);
        
        return CertificateDTO.Response.builder()
            .valid(true)
            .id(cert.getId())
            .name(cert.getStudentName())
            .issueDate(cert.getIssueDate())
            .modulesCompleted(cert.getModulesCompleted())
            .status(cert.getStatus().name())
            .message("Certificate issued successfully")
            .build();
    }
    
    public CertificateDTO.Response verifyCertificate(String id) {
        return repository.findById(id)
            .map(cert -> {
                if (cert.getStatus() == Certificate.Status.REVOKED) {
                    return buildErrorResponse("Certificate has been revoked");
                }
                
                return CertificateDTO.Response.builder()
                    .valid(true)
                    .id(cert.getId())
                    .name(cert.getStudentName())
                    .issueDate(cert.getIssueDate())
                    .modulesCompleted(cert.getModulesCompleted())
                    .status(cert.getStatus().name())
                    .message("Valid certificate")
                    .build();
            })
            .orElse(buildErrorResponse("Certificate not found"));
    }
    
    public void markLinkedInAdded(String id) {
        repository.findById(id).ifPresent(cert -> {
            cert.setLinkedinAdded(true);
            repository.save(cert);
        });
    }
    
    private String generateCertificateId() {
        long timestamp = System.currentTimeMillis();
        String random = generateRandomString(6);
        return "AICERT-" + timestamp + "-" + random;
    }
    
    private String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append(chars.charAt(random.nextInt(chars.length())));
        }
        return result.toString();
    }
    
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    private CertificateDTO.Response buildErrorResponse(String message) {
        return CertificateDTO.Response.builder()
            .valid(false)
            .message(message)
            .build();
    }
}
