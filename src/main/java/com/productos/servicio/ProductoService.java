package com.productos.servicio;

import com.productos.modelo.Producto;
import com.productos.repository.ProductoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoService {

    /**
     * Tamaño de pagina fijo para la vitrina publica: cuantos
     * productos se muestran por pagina.
     */
    public static final int PRODUCTOS_POR_PAGINA = 6;

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * Listado completo, sin paginar (lo usa el panel de administracion).
     */
    public List<Producto> listarTodos() {
        return productoRepository.findAllByOrderByNombreAsc();
    }

    /**
     * Listado paginado para la vitrina publica.
     *
     * @param pagina numero de pagina que se quiere ver, empezando en 0.
     * @return un Page<Producto> con los productos de esa pagina y
     *         metadatos (total de paginas, si hay siguiente/anterior, etc).
     */
    public Page<Producto> listarPaginado(int pagina) {
        if (pagina < 0) {
            pagina = 0;
        }
        Pageable pageable = PageRequest.of(pagina, PRODUCTOS_POR_PAGINA, Sort.by("nombre").ascending());
        return productoRepository.findAll(pageable);
    }

    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con id " + id));
    }

    @Transactional
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    @Transactional
    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }
}
