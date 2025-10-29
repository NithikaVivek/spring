package com.open.spring.mvc.certificates;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

public class CertificateDTO {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String name;
        private String email;
        private List<String> modulesCompleted;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private boolean valid;
        private String id;
        private String name;
        private LocalDateTime issueDate;
        private List<String> modulesCompleted;
        private String status;
        private String message;
    }
}