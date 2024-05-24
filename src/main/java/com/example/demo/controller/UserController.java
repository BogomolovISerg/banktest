package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.model.UserRegistrationRequest;
import com.example.demo.service.UserService;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        Optional<User> existingUser = userRepository.findByUsername(userRegistrationRequest.getUsername());
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("Имя пользователя занято.");
        }

        User newUser = userService.registerNewUser(userRegistrationRequest);
        return ResponseEntity.ok(newUser);
    }

    @PutMapping("/updatePhones")
    public ResponseEntity<?> updatePhones(Authentication authentication, @RequestBody Set<String> phones) {
        String username = authentication.getName();
        User user = userService.updatePhones(username, phones);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/updateEmails")
    public ResponseEntity<?> updateEmails(Authentication authentication, @RequestBody Set<String> emails) {
        String username = authentication.getName();
        User user = userService.updateEmails(username, emails);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/deletePhone")
    public ResponseEntity<?> deletePhone(Authentication authentication, @RequestParam String phone) {
        String username = authentication.getName();
        User user = userService.deletePhone(username, phone);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/deleteEmail")
    public ResponseEntity<?> deleteEmail(Authentication authentication, @RequestParam String email) {
        String username = authentication.getName();
        User user = userService.deleteEmail(username, email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String middleName,
            @RequestParam(required = false) String birthDate,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<User> users = userService.searchUsers(firstName, lastName, middleName, birthDate, phone, email, pageable);
        return ResponseEntity.ok(users);
    }
}
