package io.github.leoniedermeier.utils.excecption.advice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableAspectJAutoProxy
public class ApplicationForException {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationForException.class, args);
	}
	
}