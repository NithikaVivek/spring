package com.open.spring.mvc.certificates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/certificates")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CertificateApiController {
    
    @Autowired
    private CertificateService service;
    
    /**
     * POST /api/certificates - Issue new certificate
     * Body: {"name": "John Smith", "email": "john@email.com", "modulesCompleted": [...]}
     */
    @PostMapping
    public ResponseEntity<CertificateDTO.Response> issueCertificate(
            @RequestBody CertificateDTO.Request request) {
        
        CertificateDTO.Response response = service.issueCertificate(request);
        
        return response.isValid() 
            ? ResponseEntity.status(HttpStatus.CREATED).body(response)
            : ResponseEntity.badRequest().body(response);
    }
    
    /**
     * GET /api/certificates/verify/{id} - Verify certificate
     */
    @GetMapping("/verify/{id}")
    public ResponseEntity<CertificateDTO.Response> verifyCertificate(@PathVariable String id) {
        CertificateDTO.Response response = service.verifyCertificate(id);
        
        return response.isValid()
            ? ResponseEntity.ok(response)
            : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    /**
     * PUT /api/certificates/{id}/linkedin - Mark as added to LinkedIn
     */
    @PutMapping("/{id}/linkedin")
    public ResponseEntity<Void> markLinkedInAdded(@PathVariable String id) {
        service.markLinkedInAdded(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * GET /api/certificates/health - Health check
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Certificate API is running!");
    }
}
