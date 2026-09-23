package com.productos.config;

import com.productos.modelo.Categoria;
import com.productos.modelo.Producto;
import com.productos.repository.CategoriaRepository;
import com.productos.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Carga datos de prueba apenas arranca la aplicacion.
 *
 * Como la base H2 es en memoria y se recrea en cada arranque
 * (spring.jpa.hibernate.ddl-auto=create-drop), este componente
 * corre siempre al iniciar y deja el catalogo listo para usar
 * sin que haya que hacer nada manualmente.
 *
 * CommandLineRunner es una interfaz de Spring Boot con un solo
 * metodo (run) que se ejecuta automaticamente una vez que el
 * contexto de la aplicacion terminó de inicializarse.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    public DataInitializer(CategoriaRepository categoriaRepository, ProductoRepository productoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public void run(String... args) {
        // Si ya hay categorias cargadas, no volvemos a insertar nada
        // (por las dudas de que este metodo se llame mas de una vez).
        if (categoriaRepository.count() > 0) {
            return;
        }

        Categoria aromas = categoriaRepository.save(new Categoria("Aromas"));
        Categoria textiles = categoriaRepository.save(new Categoria("Textiles"));
        Categoria hogar = categoriaRepository.save(new Categoria("Hogar"));
        Categoria aromaterapia = categoriaRepository.save(new Categoria("Aromaterapia"));
        Categoria aceite = categoriaRepository.save(new Categoria("Aceite"));
        Categoria ropa = categoriaRepository.save(new Categoria("Ropa"));

        productoRepository.save(new Producto(
                "Sahumerios Triple Combinado", 2500.0,
                "Combinacion de tres fragancias en una sola varilla, aroma intenso y duradero.",
                40, "/images/sahumerios-triple.jpeg", aromas));

        productoRepository.save(new Producto(
                "Perfume Textil", 3200.0,
                "Perfume especial para ropa y textiles, deja un aroma suave y agradable.",
                25, "/images/perfume-textil.jpeg", textiles));

        productoRepository.save(new Producto(
                "Difusores de Ambiente", 4800.0,
                "Difusor con varillas de bambu para perfumar ambientes de forma constante.",
                18, "/images/difusores.jpeg", hogar));

        productoRepository.save(new Producto(
                "Fragancias Saphirus", 2900.0,
                "Fragancia concentrada de alta calidad, ideal para recargar difusores.",
                30, "/images/fragancias-saphirus.jpeg", aromas));

        productoRepository.save(new Producto(
                "Bombas Aromaticas", 1800.0,
                "Bombas efervescentes con aceites esenciales para perfumar espacios chicos.",
                50, "/images/bombas-aromaticas.jpeg", aromaterapia));

        productoRepository.save(new Producto(
                "Pastillas de Limpieza", 1500.0,
                "Pastillas perfumadas para inodoros y ambientes, larga duracion.",
                60, "/images/pastillas-limpieza.jpeg", hogar));

        productoRepository.save(new Producto(
                "Aceite para Hornillo", 2100.0,
                "Aceite esencial puro para usar en hornillos aromaticos.",
                22, "/images/aceite-hornillo.jpeg", aceite));

        productoRepository.save(new Producto(
                "Sahumerios Canabis", 2600.0,
                "Sahumerio de aroma herbal intenso, ideal para relajar ambientes.",
                35, "/images/sahumerios-canabis.jpeg", aromas));

        productoRepository.save(new Producto(
                "Textiles", 3600.0,
                "Textil decorativo con toque aromatico, perfecto para regalar.",
                15, "/images/textiles.jpeg", ropa));
    }
}
