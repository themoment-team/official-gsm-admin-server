package com.themoment.officialgsm.domain.Admin.repository;

import com.themoment.officialgsm.domain.Admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, String> {
    Optional<Admin> findAdminByName(String name);
}
