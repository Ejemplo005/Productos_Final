package com.productos.repository;

import com.productos.modelo.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Al extender JpaRepository<Categoria, Long> ya obtenemos gratis:
 * findAll(), findById(), save(), deleteById(), count(), etc.
 * No hace falta escribir ni una linea de SQL ni de JPQL.
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
