package com.productos.controller;

import com.productos.modelo.Categoria;
import com.productos.modelo.Producto;
import com.productos.repository.CategoriaRepository;
import com.productos.servicio.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Panel de administracion: alta, edicion y borrado de productos.
 *
 * IMPORTANTE (transparencia con los alumnos): este panel NO tiene
 * login ni Spring Security todavia. Cualquiera que conozca la URL
 * /admin puede editar el catalogo. Se deja asi a proposito, para no
 * mezclar dos temas (JPA/Spring Boot y seguridad) en el mismo
 * ejercicio: agregar autenticacion queda como posible siguiente paso
 * (ver README).
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductoService productoService;
    private final CategoriaRepository categoriaRepository;

    public AdminController(ProductoService productoService, CategoriaRepository categoriaRepository) {
        this.productoService = productoService;
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping
    public String panel(Model model) {
        cargarListasComunes(model);
        model.addAttribute("producto", new Producto());
        return "admin";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        cargarListasComunes(model);
        model.addAttribute("producto", productoService.buscarPorId(id));
        return "admin";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam(required = false) Long id,
                           @RequestParam String nombre,
                           @RequestParam double precio,
                           @RequestParam(required = false) String descripcion,
                           @RequestParam int stock,
                           @RequestParam Long categoriaId,
                           @RequestParam(required = false) String imagenUrl) {

        Producto producto = (id == null) ? new Producto() : productoService.buscarPorId(id);
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada con id " + categoriaId));

        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setDescripcion(descripcion);
        producto.setStock(stock);
        producto.setCategoria(categoria);
        if (imagenUrl != null && !imagenUrl.isBlank()) {
            producto.setImagenUrl(imagenUrl);
        }

        productoService.guardar(producto);
        return "redirect:/admin";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return "redirect:/admin";
    }

    private void cargarListasComunes(Model model) {
        model.addAttribute("productos", productoService.listarTodos());
        model.addAttribute("categorias", categoriaRepository.findAll());
    }
}
