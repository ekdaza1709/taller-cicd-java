package com.example.steps;

import io.cucumber.java.es.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class AppSteps {
  static class Usuario {
    String user;
    String pass;
    Usuario(String u, String p) { user = u; pass = p; }
  }

  private final Map<String, Usuario> usuarios = new HashMap<>();
  private boolean autenticado = false;
  private String ultimoError = null;
  private final List<Map<String, String>> tareas = new ArrayList<>();
  private List<Map<String, String>> vista = new ArrayList<>();
  private String usuarioActual = null;

  // Autenticación
  @Dado("un usuario registrado {string} con contraseña {string}")
  public void registrarUsuario(String u, String p) {
    usuarios.put(u, new Usuario(u, p));
  }

  @Cuando("intento iniciar sesión con usuario {string} y contraseña {string}")
  public void intentoLogin(String u, String p) {
    Usuario reg = usuarios.get(u);
    if (reg != null && Objects.equals(reg.pass, p)) {
      autenticado = true;
      ultimoError = null;
      usuarioActual = u;
    } else {
      autenticado = false;
      ultimoError = "Credenciales inválidas";
      usuarioActual = null;
    }
  }

  @Entonces("el acceso es concedido")
  public void accesoConcedido() {
    assertTrue(autenticado);
  }

  @Entonces("el acceso es denegado con mensaje {string}")
  public void accesoDenegado(String msg) {
    assertFalse(autenticado);
    assertEquals(msg, ultimoError);
  }

  @Y("el estado de sesión es {string}")
  public void estadoSesion(String esperado) {
    assertEquals("autenticado", esperado);
    assertTrue(autenticado);
  }

  // Alta de tareas
  @Dado("que estoy autenticado como {string}")
  public void autenticadoComo(String u) {
    usuarios.putIfAbsent(u, new Usuario(u, "dummy"));
    usuarioActual = u;
    autenticado = true;
    ultimoError = null;
  }

  @Cuando("creo una tarea con título {string}")
  public void crearTarea(String titulo) {
    if (titulo == null || titulo.isBlank()) {
      ultimoError = "Título requerido";
      return;
    }
    Map<String, String> t = new HashMap<>();
    t.put("owner", usuarioActual);
    t.put("titulo", titulo);
    t.put("estado", "pendiente");
    tareas.add(t);
  }

  @Cuando("intento crear una tarea con título {string}")
  public void intentoCrearTarea(String titulo) {
    crearTarea(titulo);
  }

  @Entonces("la tarea existe en mi lista")
  public void tareaExiste() {
    assertTrue(tareas.stream().anyMatch(t -> t.get("owner").equals(usuarioActual)));
  }

  @Entonces("su estado es {string}")
  public void estadoTarea(String estado) {
    assertEquals(estado, tareas.get(tareas.size() - 1).get("estado"));
  }

  @Entonces("se muestra un error {string}")
  public void seMuestraError(String esperado) {
    assertEquals(esperado, ultimoError);
  }

  // Listado y filtro
  @Y("existen las tareas:")
  public void existenTareas(io.cucumber.datatable.DataTable table) {
    for (var row : table.asMaps()) {
      Map<String, String> t = new HashMap<>(row);
      t.put("owner", usuarioActual);
      tareas.add(t);
    }
  }

  @Cuando("consulto mis tareas")
  public void consultoMisTareas() {
    vista = tareas.stream().filter(t -> t.get("owner").equals(usuarioActual)).toList();
  }

  @Cuando("filtro mis tareas por {string}")
  public void filtroMisTareasPor(String estado) {
    consultoMisTareas();
    vista = vista.stream().filter(t -> estado.equals(t.get("estado"))).toList();
  }

  @Entonces("veo {int} tarea\\(s)")
  public void veoN(int n) {
    assertEquals(n, vista.size());
  }
}
