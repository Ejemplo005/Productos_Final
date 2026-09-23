# Aguas Astrales — Tienda de Productos (Spring Boot + Thymeleaf + H2)

Proyecto que une, en una sola aplicacion Spring Boot, el backend de
persistencia con JPA (entidades `Producto` y `Categoria`) con la
vitrina de productos original en HTML y CSS ("Aguas Astrales"),
renderizada con Thymeleaf del lado del servidor.

## 1. Que trae este proyecto

- Backend con **Spring Data JPA** (entidades, repositorios, servicios).
- Base de datos **H2 en memoria**: no requiere instalar MySQL ni ningun
  otro motor de base de datos. Se crea sola al arrancar la aplicacion.
- Vistas en **HTML y CSS** con **Thymeleaf** (no es una SPA, no usa
  JavaScript de framework, sigue siendo HTML "de siempre" pero generado
  por el servidor con datos reales).
- Vitrina publica (`/`) con **paginacion** de productos.
- **Panel de administracion** (`/admin`) para crear, editar y borrar
  productos, sin login (ver seccion 6).
- Datos de ejemplo precargados automaticamente al iniciar
  (`DataInitializer`).

## 2. Como correrlo

**No hace falta instalar ninguna base de datos.** La base H2 vive en
memoria, dentro del mismo proceso de la aplicacion: se crea cuando
arranca y se destruye cuando se detiene.

### Requisitos

- JDK 21 instalado.
- Gradle (o usar el proyecto desde un IDE como IntelliJ, que trae
  Gradle integrado).

### Pasos

1. Abrir una terminal en la carpeta del proyecto.
2. Ejecutar:
   ```bash
   gradle bootRun
   ```
   (o, si el proyecto se abre desde IntelliJ/Eclipse, correr la clase
   `ProductosSpringbootApplication` directamente con el boton de "Run").
3. Esperar a que en la consola aparezca algo como
   `Started ProductosSpringbootApplication in X seconds`.
4. Abrir el navegador en:
   - **Vitrina de productos:** http://localhost:8080/
   - **Panel de administracion:** http://localhost:8080/admin
   - **Consola de H2** (opcional, para ver la base de datos en vivo):
     http://localhost:8080/h2-console
     - JDBC URL: `jdbc:h2:mem:tienda_sahumerios`
     - Usuario: `sa`
     - Password: (dejar vacio)

Cada vez que se reinicia la aplicacion, la base se recrea desde cero y
`DataInitializer` vuelve a cargar los productos de ejemplo. Esto es
intencional: pensado para clase y para pruebas, sin depender de tener
una base de datos ya instalada en la máquina de cada alumno.

## 3. Estructura del proyecto

```
src/main/java/com/productos/
├── ProductosSpringbootApplication.java   Clase principal (main)
├── modelo/
│   ├── Categoria.java                    Entidad Categoria
│   └── Producto.java                     Entidad Producto
├── repository/
│   ├── CategoriaRepository.java          JpaRepository<Categoria, Long>
│   └── ProductoRepository.java           JpaRepository<Producto, Long>
├── servicio/
│   └── ProductoService.java              Reglas de negocio + paginacion
├── controller/
│   ├── ProductoController.java           Vitrina publica ("/", "/productos")
│   └── AdminController.java              Panel admin ("/admin/**")
└── config/
    └── DataInitializer.java              Carga datos de ejemplo al iniciar

src/main/resources/
├── application.properties                Configuracion (H2, JPA, puerto)
├── templates/
│   ├── productos.html                    Vitrina publica (Thymeleaf)
│   └── admin.html                        Panel de administracion
└── static/
    ├── css/estilos.css                   Estilos de toda la app
    └── images/                           Fotos de los productos
```

## 4. Base de datos: de MySQL a H2

La version anterior de este proyecto usaba MySQL, lo que obligaba a
tener el servidor corriendo en `localhost:3306` antes de poder probar
nada. Se reemplazo por H2 en memoria en `application.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:tienda_sahumerios;DB_CLOSE_DELAY=-1
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.h2.console.enabled=true
```

| Concepto | MySQL (antes) | H2 en memoria (ahora) |
|---|---|---|
| Instalacion | Servidor externo (XAMPP, MySQL Server, etc.) | Ninguna, viene como dependencia Java |
| `ddl-auto` | `update` (conserva datos entre reinicios) | `create-drop` (se recrea cada vez) |
| Ver los datos | Workbench / DBeaver / consola mysql | Consola web incluida en `/h2-console` |
| Uso tipico | Produccion / datos persistentes | Desarrollo, pruebas, clase |

Si en el futuro se quiere volver a una base persistente, alcanza con
agregar de nuevo la dependencia del conector (por ejemplo
`com.mysql:mysql-connector-j`) y las propiedades de conexion
correspondientes en `application.properties`.

## 5. Paginacion de productos

La vitrina (`/`) ya no muestra todos los productos en una sola lista:
los reparte en paginas de a **6 productos** (`ProductoService.PRODUCTOS_POR_PAGINA`).

- `ProductoRepository` no necesito ningun metodo nuevo: `JpaRepository`
  ya incluye `findAll(Pageable pageable)`, que devuelve un `Page<Producto>`.
- `ProductoService.listarPaginado(int pagina)` construye un
  `PageRequest.of(pagina, tamanio, Sort.by("nombre"))` y lo pasa al
  repositorio.
- `ProductoController` lee el numero de pagina de la URL
  (`?pagina=0`, `?pagina=1`, ...) con `@RequestParam(defaultValue = "0")`
  y le pasa a la vista el listado de esa pagina junto con el total de
  paginas.
- `productos.html` dibuja los botones de paginacion (anterior /
  numeros / siguiente) usando `totalPaginas` y `paginaActual`.

Para cambiar cuantos productos se muestran por pagina, alcanza con
modificar la constante `PRODUCTOS_POR_PAGINA` en `ProductoService`.

## 6. Redes sociales en el footer

El sitio original (`tienda-productos12/index2.html`) ya tenia enlaces
a Instagram y Facebook, pero con HTML mal formado (el atributo
`target` tenia texto extra pegado adentro, por ejemplo
`target="_blank rel= Aguas.astrales"`, que ni siquiera es un atributo
`rel` valido). En este proyecto se corrigieron y se movieron al
`<footer>` de la vitrina, con iconos SVG propios y atributos correctos:

```html
<a href="https://www.instagram.com/aguas.astrales" target="_blank" rel="noopener noreferrer">Instagram</a>
<a href="https://www.facebook.com/aguas.astrales" target="_blank" rel="noopener noreferrer">Facebook</a>
```

`rel="noopener noreferrer"` es una buena practica al abrir enlaces
externos en una pestaña nueva (`target="_blank"`): evita que la pagina
externa pueda acceder a la ventana original.

## 7. Panel de administracion sin login (a proposito)

El panel `/admin` permite crear, editar y borrar productos, pero **no
tiene ningun sistema de autenticacion**. Cualquiera que conozca la URL
puede modificar el catalogo. Esto es intencional en este ejercicio,
para no mezclar el tema de JPA/Spring Boot con el de seguridad
(Spring Security) en la misma entrega. Es el primer punto pendiente
en la seccion "Proximos pasos".

## 8. Conceptos de JPA/Spring aplicados (mapa rapido)

| Concepto | Donde se usa |
|---|---|
| `@Entity`, `@Table`, `@Column` | `modelo/Categoria.java`, `modelo/Producto.java` |
| `@Id`, `@GeneratedValue(IDENTITY)` | Clave primaria de ambas entidades |
| `@OneToMany(mappedBy=...)` / `@ManyToOne` / `@JoinColumn` | Relacion Categoria ↔ Producto |
| `JpaRepository<T, ID>` | `repository/CategoriaRepository.java`, `repository/ProductoRepository.java` |
| Query method derivado | `findAllByOrderByNombreAsc()` |
| `Pageable` / `Page<T>` | Paginacion en `ProductoService` y `ProductoController` |
| `@Service`, `@Transactional` | `servicio/ProductoService.java` |
| `@Controller`, `@GetMapping`, `@PostMapping`, `@RequestParam`, `@PathVariable` | `controller/ProductoController.java`, `controller/AdminController.java` |
| `CommandLineRunner` | `config/DataInitializer.java` (carga de datos de ejemplo) |
| Thymeleaf (`th:each`, `th:if`, `th:unless`, `th:text`, `@{...}`) | `templates/productos.html`, `templates/admin.html` |

## 9. Proximos pasos posibles

- Agregar autenticacion al panel `/admin` con Spring Security (login
  con usuario/contraseña, o roles ADMIN/USER).
- Persistir la base entre reinicios (volver a MySQL/PostgreSQL, o
  H2 en modo archivo en vez de memoria).
- Búsqueda y filtro de productos por nombre o categoria en la vitrina.
- Subida de imagenes de productos desde el panel, en vez de escribir
  la ruta a mano.
- Tests automáticos de los controllers (`@WebMvcTest`) y del service.

---

Hecho para la materia de Instituto Celsius / UTN — proyecto
"Aguas Astrales", combinando la guia de JPA, la de bases de datos y la
de Spring Boot con frontend.
