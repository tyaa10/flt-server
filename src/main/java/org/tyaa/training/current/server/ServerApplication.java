package org.tyaa.training.current.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.tyaa.training.current.server.entities.RoleEntity;
import org.tyaa.training.current.server.repositories.RoleRepository;

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
	public CommandLineRunner initData (RoleRepository roleRepository) {
		return args -> {
			roleRepository.truncateTable();
			roleRepository.save(RoleEntity.builder().name("ROLE_ADMIN").build());
			roleRepository.save(RoleEntity.builder().name("ROLE_CUSTOMER").build());
		};
	}
}
