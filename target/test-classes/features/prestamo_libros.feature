# language: es
Característica: Préstamo de libros
  Como lector activo quiero solicitar libros disponibles.

  Escenario: Préstamo exitoso de libro disponible
    Dado que estoy identificado como lector activo "L-101"
    Y el libro "Clean Code" tiene estado "disponible"
    Cuando solicito el préstamo del libro "Clean Code"
    Entonces el préstamo es aprobado
    Y el estado del libro "Clean Code" pasa a "prestado"

  Escenario: Impedir préstamo de libro ya prestado
    Dado que estoy identificado como lector activo "L-101"
    Y el libro "Design Patterns" tiene estado "prestado"
    Cuando solicito el préstamo del libro "Design Patterns"
    Entonces se muestra el mensaje de error "Libro no disponible"
