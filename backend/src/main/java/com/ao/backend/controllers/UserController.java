package com.ao.backend.controllers;

import com.ao.backend.models.AppUser;
import com.ao.backend.models.dto.Dto;
import com.ao.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/users")
@CrossOrigin (origins = "http://localhost:3000")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/current")
    public ResponseEntity<Dto> getUserDetails(
            HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok().body(userService.getUserByUsername(username));
    }
}
