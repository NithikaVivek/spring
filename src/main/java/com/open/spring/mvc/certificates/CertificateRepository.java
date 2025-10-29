package com.open.spring.mvc.certificates;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, String> {
    Optional<Certificate> findByEmail(String email);
    boolean existsByEmail(String email);
}