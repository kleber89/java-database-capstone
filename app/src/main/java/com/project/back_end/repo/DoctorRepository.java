package com.project.back_end.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.project.back_end.models.Doctor;
import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
   // Buscar por email exacto
   Doctor findByEmail(String email);

   // Búsqueda flexible por nombre usando LIKE y CONCAT
   @Query("SELECT d FROM Doctor d WHERE d.name LIKE CONCAT('%', :name, '%')")
   List<Doctor> findByNameLike(String name);

   // Búsqueda por nombre parcial (ignorando mayúsculas/minúsculas) y especialidad
   // exacta (ignorando mayúsculas/minúsculas)
   @Query("SELECT d FROM Doctor d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%')) AND LOWER(d.specialty) = LOWER(:specialty)")
   List<Doctor> findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(String name, String specialty);

   // Buscar por especialidad ignorando mayúsculas/minúsculas
   List<Doctor> findBySpecialtyIgnoreCase(String specialty);
}