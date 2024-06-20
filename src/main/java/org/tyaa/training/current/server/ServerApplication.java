package org.tyaa.training.current.server;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.tyaa.training.current.server.seeders.interfaces.ISeeder;

import java.util.List;

/**
 * Главный класс приложения
 * */
@SpringBootApplication
public class ServerApplication {

	/**
	 * Стандартная точка входа в приложение
	 * */
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	/**
	 * Инициализация БД
	 * */
	@Bean
	public CommandLineRunner initData (List<ISeeder> seeders) {
		return args -> seeders.forEach(ISeeder::seed);
	}
}
