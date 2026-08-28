package com.medicine.management.repository;
import com.medicine.management.domain.entity.AppUser;
import java.util.Optional; import org.springframework.data.jpa.repository.JpaRepository;
public interface AppUserRepository extends JpaRepository<AppUser,Long>{ Optional<AppUser> findByUsername(String username); }
