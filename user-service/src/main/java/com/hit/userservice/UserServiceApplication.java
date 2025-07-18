package com.hit.userservice;

import com.hit.userservice.domain.entity.ERole;
import com.hit.userservice.domain.entity.Role;
import com.hit.userservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Arrays;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing
@EnableCaching
@RequiredArgsConstructor
public class UserServiceApplication {

	private final RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase() {
		return args -> {
			Arrays.stream(ERole.values()).forEach(roleEnum -> {
				if (roleRepository.findByName(roleEnum).isEmpty()) {
					roleRepository.save(new Role(roleEnum));
				}
			});
		};
	}
}
