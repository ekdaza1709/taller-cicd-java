# language: es
Característica: Registro de nuevos lectores
  Como administrador necesito registrar lectores para habilitarles préstamos.

  Escenario: Registro exitoso de lector
    Dado que no existe un lector con carné "L-101"
    Cuando registro al lector con carné "L-101" y nombre "Carlos Pérez"
    Entonces el lector "L-101" queda registrado con estado "activo"

  Escenario: Rechazar registro con carné duplicado
    Dado que existe un lector registrado con carné "L-102" y nombre "Ana Gómez"
    Cuando intento registrar al lector con carné "L-102" y nombre "Luis Díaz"
    Entonces se muestra el mensaje de error "Carné ya registrado"

