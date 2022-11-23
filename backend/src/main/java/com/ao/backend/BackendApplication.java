package com.ao.backend;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.ao.backend.services.UserService;
import com.ao.backend.repositories.RoleRepository;
import com.ao.backend.models.Role;
import com.ao.backend.models.dto.RegisterUserDTO;

@SpringBootApplication
@RequiredArgsConstructor
public class BackendApplication {
	private final RoleRepository roleRepository;
	private final UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner run() {
		return args -> {
			if (roleRepository.findAll().isEmpty()) {
				roleRepository.save(new Role("ROLE_USER"));
				roleRepository.save(new Role("ROLE_ADMIN"));
				roleRepository.save(new Role("ROLE_EMPLOYEE"));

				userService.saveUser(new RegisterUserDTO("adminAdmin", "testTest1.", "Admin admin"));
				userService.saveUser(new RegisterUserDTO("userUser", "testTest1.", "User user"));

				userService.addRoleToUser("adminAdmin", "ROLE_ADMIN");
				userService.addRoleToUser("adminAdmin", "ROLE_EMPLOYEE");
			}
		};
	}
}
