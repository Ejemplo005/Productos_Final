package com.productos.controller;

import com.productos.modelo.Producto;
import com.productos.servicio.ProductoService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller de la vitrina publica (lo que ve cualquier visitante,
 * sin login). Devuelve el nombre de una plantilla Thymeleaf
 * ("productos"), no JSON: por eso es @Controller y no @RestController.
 */
@Controller
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    /**
     * "/" y "/productos" muestran lo mismo: la vitrina de productos.
     * El parametro "pagina" es opcional (?pagina=1, ?pagina=2, ...);
     * si no viene en la URL, arranca en la pagina 0 (la primera).
     */
    @GetMapping({"/", "/productos"})
    public String productos(@RequestParam(name = "pagina", defaultValue = "0") int pagina, Model model) {
        Page<Producto> paginaDeProductos = productoService.listarPaginado(pagina);

        model.addAttribute("paginaDeProductos", paginaDeProductos);
        model.addAttribute("productos", paginaDeProductos.getContent());
        model.addAttribute("paginaActual", paginaDeProductos.getNumber());
        model.addAttribute("totalPaginas", paginaDeProductos.getTotalPages());
        model.addAttribute("totalProductos", paginaDeProductos.getTotalElements());

        return "productos";
    }
}
