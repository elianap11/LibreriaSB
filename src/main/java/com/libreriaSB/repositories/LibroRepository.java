package com.libreriaSB.repositories;

import com.libreriaSB.entities.Autor;
import com.libreriaSB.entities.Libro;
import com.libreriaSB.entities.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepository extends JpaRepository<Libro, String> {

    @Modifying
    @Query("UPDATE Libro l SET l.isbn = :isbn, l.titulo = :titulo, l.anio = :anio, l.ejemplares = :ejemplares, l.ejemplaresPrestados = :ejemplaresPrestados, l.ejemplaresRestantes = :ejemplaresRestantes, l.autor = :autor, l.editorial = :editorial WHERE l.id = :id")
    void modificarLibro(@Param("id") String id, @Param("isbn") Long isbn, @Param("titulo") String titulo, @Param("anio") Integer anio, @Param("ejemplares") Integer ejemplares, @Param("ejemplaresPrestados") Integer ejemplaresPrestados, @Param("ejemplaresRestantes") Integer ejemplaresRestantes, @Param("autor") Autor author, @Param("editorial") Editorial editorial);

    @Modifying
    @Query("UPDATE Libro l SET l.alta = true WHERE l.id = :id")
    void darDeAlta(@Param("id") String id);

    @Query("SELECT a FROM Libro a WHERE a.isbn = :isbn")
    Libro buscarIsbn(@Param("isbn") Long isbn);
}
