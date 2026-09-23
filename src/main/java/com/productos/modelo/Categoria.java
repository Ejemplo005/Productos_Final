package com.productos.modelo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa una categoria de productos
 * (por ejemplo: "Aromas", "Textiles", "Hogar").
 *
 * @Entity  -> le dice a JPA que esta clase se mapea a una tabla.
 * @Table   -> nombre concreto de la tabla en la base de datos.
 */
@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 60)
    private String nombre;

    /**
     * Lado "inverso" de la relacion con Producto.
     * mappedBy = "categoria" indica que la relacion la controla
     * el atributo "categoria" de la clase Producto (dueño de la FK).
     * cascade = ALL: si se borra una categoria, se propagan los
     * cambios a sus productos (en este proyecto, en la practica,
     * primero hay que reasignar o borrar los productos).
     */
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    private List<Producto> productos = new ArrayList<>();

    public Categoria() {
    }

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    @Override
    public String toString() {
        return "Categoria{id=" + id + ", nombre='" + nombre + "'}";
    }
}
