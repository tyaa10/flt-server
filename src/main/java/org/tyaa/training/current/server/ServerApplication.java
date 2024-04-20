package org.tyaa.training.current.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.tyaa.training.current.server.entities.RoleEntity;
import org.tyaa.training.current.server.entities.UserEntity;
import org.tyaa.training.current.server.repositories.RoleRepository;
import org.tyaa.training.current.server.repositories.UserRepository;

import java.util.List;

/**
 * Главный класс приложения
 * */
@SpringBootApplication
public class ServerApplication {

	@Value("${custom.init-data.roles}")
	private List<String> roles;

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	/**
	 * Инициализация БД
	 * */
	@Bean
	public CommandLineRunner initData (RoleRepository roleRepository,
									   UserRepository userRepository,
									   PasswordEncoder passwordEncoder) {
		return args -> {
			// roleRepository.truncateTable();
			/* Обеспечение наличия в БД ролей пользователей */
			for (String roleName : roles) {
				roleRepository.save(RoleEntity.builder().name(roleName).build());
			}
			/* Обеспечение наличия в БД нескольких фейковых пользователей для отладки и тестирования приложения */
			RoleEntity adminRole = roleRepository.findRoleByName(roles.get(0));
			RoleEntity userRole = roleRepository.findRoleByName(roles.get(1));
			userRepository.save(
					UserEntity.builder()
							.name("admin")
							.password(passwordEncoder.encode("AdminPassword1"))
							.role(adminRole)
							.build()
			);
			userRepository.save(
					UserEntity.builder()
							.name("one")
							.password(passwordEncoder.encode("UserPassword1"))
							.role(userRole)
							.build()
			);
			userRepository.save(
					UserEntity.builder()
							.name("two")
							.password(passwordEncoder.encode("UserPassword2"))
							.role(userRole)
							.build()
			);
			userRepository.save(
					UserEntity.builder()
							.name("three")
							.password(passwordEncoder.encode("UserPassword3"))
							.role(userRole)
							.build()
			);
		};
	}
}
