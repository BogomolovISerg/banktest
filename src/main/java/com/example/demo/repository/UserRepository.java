package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE " +
            "(:firstName IS NULL OR u.firstName LIKE %:firstName%) AND " +
            "(:lastName IS NULL OR u.lastName LIKE %:lastName%) AND " +
            "(:middleName IS NULL OR u.middleName LIKE %:middleName%) AND " +
            "(:birthDate IS NULL OR u.birthDate > :birthDate) AND " +
            "(:phone IS NULL OR :phone MEMBER OF u.phones) AND " +
            "(:email IS NULL OR :email MEMBER OF u.emails)")
    Page<User> searchUsers(String firstName, String lastName, String middleName, String birthDate, String phone, String email, Pageable pageable);
}
