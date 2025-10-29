package com.open.spring.mvc.certificates;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "certificates")
public class Certificate {
    
    @Id
    private String id;  // AICERT-timestamp-random
    
    @Column(nullable = false)
    private String studentName;
    
    @Column(nullable = false)
    private String email;
    
    @Column(nullable = false)
    private LocalDateTime issueDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;
    
    @ElementCollection
    @CollectionTable(name = "certificate_modules", 
                     joinColumns = @JoinColumn(name = "certificate_id"))
    @Column(name = "module_name")
    private List<String> modulesCompleted = new ArrayList<>();
    
    private boolean linkedinAdded = false;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (issueDate == null) {
            issueDate = LocalDateTime.now();
        }
    }
    
    public enum Status {
        ACTIVE,
        REVOKED
    }
}
