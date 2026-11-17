package com.example.steps;

import io.cucumber.java.es.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class AppSteps {

  private final Map<String, String> lectores = new HashMap<>(); // carne -> estado
  private final Map<String, String> librosEstado = new HashMap<>(); // titulo -> estado
  private final List<Map<String, String>> catalogo = new ArrayList<>();
  private List<Map<String, String>> vistaCatalogo = new ArrayList<>();

  private String lectorActual = null;
  private boolean prestamoAprobado = false;
  private String ultimoError = null;

  // HU-01: Registro de lectores
  @Dado("que no existe un lector con carné {string}")
  public void noExisteLector(String carne) {
    lectores.remove(carne);
  }

  @Dado("que existe un lector registrado con carné {string} y nombre {string}")
  public void existeLector(String carne, String nombre) {
    lectores.put(carne, "activo");
  }

  @Cuando("registro al lector con carné {string} y nombre {string}")
  public void registrarLector(String carne, String nombre) {
    if (lectores.containsKey(carne)) {
      ultimoError = "Carné ya registrado";
    } else {
      lectores.put(carne, "activo");
      ultimoError = null;
    }
  }

  @Cuando("intento registrar al lector con carné {string} y nombre {string}")
  public void intentoRegistrarLector(String carne, String nombre) {
    registrarLector(carne, nombre);
  }

  @Entonces("el lector {string} queda registrado con estado {string}")
  public void verificarLectorRegistrado(String carne, String estadoEsperado) {
    assertEquals(estadoEsperado, lectores.get(carne));
  }

  @Entonces("se muestra el mensaje de error {string}")
  public void verificarMensajeError(String mensajeEsperado) {
    assertEquals(mensajeEsperado, ultimoError);
  }

  // HU-02: Préstamo de libros
  @Dado("que estoy identificado como lector activo {string}")
  public void identificarLectorActivo(String carne) {
    lectores.put(carne, "activo");
    lectorActual = carne;
    ultimoError = null;
  }

  @Y("el libro {string} tiene estado {string}")
  public void configurarEstadoLibro(String titulo, String estado) {
    librosEstado.put(titulo, estado);
  }

  @Cuando("solicito el préstamo del libro {string}")
  public void solicitarPrestamo(String titulo) {
    String estado = librosEstado.get(titulo);
    if ("activo".equals(lectores.get(lectorActual)) && "disponible".equals(estado)) {
      prestamoAprobado = true;
      librosEstado.put(titulo, "prestado");
      ultimoError = null;
    } else {
      prestamoAprobado = false;
      ultimoError = "Libro no disponible";
    }
  }

  @Entonces("el préstamo es aprobado")
  public void verificarPrestamoAprobado() {
    assertTrue(prestamoAprobado);
  }

  @Y("el estado del libro {string} pasa a {string}")
  public void verificarEstadoLibro(String titulo, String estadoEsperado) {
    assertEquals(estadoEsperado, librosEstado.get(titulo));
  }

  // HU-03: Catálogo y filtro
  @Dado("que existen los siguientes libros en el catálogo:")
  public void cargarCatalogo(io.cucumber.datatable.DataTable table) {
    catalogo.clear();
    for (var fila : table.asMaps()) {
      catalogo.add(new HashMap<>(fila));
    }
  }

  @Cuando("consulto el catálogo completo")
  public void consultarCatalogo() {
    vistaCatalogo = new ArrayList<>(catalogo);
  }

  @Cuando("filtro el catálogo por categoría {string}")
  public void filtrarCatalogo(String categoria) {
    vistaCatalogo = catalogo.stream()
        .filter(libro -> categoria.equals(libro.get("categoria")))
        .toList();
  }

  @Entonces("veo {int} libro\\(s) en la lista")
  public void verificarCantidadLibros(int cantidadEsperada) {
    assertEquals(cantidadEsperada, vistaCatalogo.size());
  }
}
