package com.productos.repository;

import com.productos.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Query method derivado: Spring Data JPA lee el nombre del metodo
     * y genera automaticamente la consulta
     * "SELECT p FROM Producto p ORDER BY p.nombre ASC".
     * Se usa en el panel de administracion, donde se listan todos
     * los productos sin paginar.
     */
    List<Producto> findAllByOrderByNombreAsc();

    /**
     * Para la vitrina publica NO hace falta un metodo propio: JpaRepository
     * ya trae "findAll(Pageable pageable)" incluido, que devuelve un
     * Page<Producto> (una "pagina" con la porcion de resultados, mas
     * informacion util como el total de paginas). El orden se indica
     * al construir el Pageable en el service, con Sort.by("nombre").
     */
}
