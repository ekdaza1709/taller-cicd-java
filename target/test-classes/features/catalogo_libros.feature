# language: es
Característica: Consulta y filtrado del catálogo de libros

  Antecedentes:
    Dado que existen los siguientes libros en el catálogo:

      | titulo               | categoria  |
      | Clean Code           | Tecnología |
      | Cien años de soledad | Literatura |
      | Refactoring          | Tecnología |

  Escenario: Listar todo el catálogo
    Cuando consulto el catálogo completo
    Entonces veo 3 libro(s) en la lista

  Escenario: Filtrar libros por categoría
    Cuando filtro el catálogo por categoría "Tecnología"
    Entonces veo 2 libro(s) en la lista
