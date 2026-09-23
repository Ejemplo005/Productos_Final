package com.productos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicacion.
 *
 * @SpringBootApplication hace tres cosas a la vez:
 *  - @Configuration: esta clase puede definir configuracion de Spring.
 *  - @EnableAutoConfiguration: Spring Boot configura automaticamente
 *    JPA, el servidor web, Thymeleaf, etc. en base a las dependencias
 *    declaradas en build.gradle.
 *  - @ComponentScan: Spring busca @Component, @Service, @Repository
 *    y @Controller dentro del paquete com.productos y sus subpaquetes.
 */
@SpringBootApplication
public class ProductosSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductosSpringbootApplication.class, args);
    }
}
