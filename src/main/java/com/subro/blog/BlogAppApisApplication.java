package com.subro.blog;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
public class BlogAppApisApplication implements CommandLineRunner {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(BlogAppApisApplication.class, args);
	}

    /**
     * Provides a ModelMapper bean to map between DTOs and entities.
     *
     * @return a new instance of ModelMapper
     */
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	/**
	 * The main entry point for the application when it is run from the command line.
	 * <p>
	 * This method is called by Spring Boot when the application is run from the command line.
	 * It is used to initialize the application and to run any necessary setup code.
	 * <p>
	 * For this application, the method does nothing except print a message to the console.
	 *
	 * @param args the command line arguments passed to the application
	 */
	@Override
	public void run(String... args) throws Exception {
		System.out.println(this.passwordEncoder.encode("ui88777ijh77j"));
	}
}