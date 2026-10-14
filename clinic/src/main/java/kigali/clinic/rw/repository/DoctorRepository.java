package kigali.clinic.rw.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Office;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    boolean existsByOffice(Office office);
    Optional<Doctor> findByOffice(Office office);
    boolean existsByOffice_OfficeNumber(int officeNumber);

    @Query("SELECT d FROM Doctor d WHERE LOWER(d.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(d.lastName) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY d.lastName ASC")
    List<Doctor> searchByName(@Param("name") String name);

    @Query("SELECT d FROM Doctor d JOIN d.specializations s WHERE LOWER(s.name) = LOWER(:name) ORDER BY d.lastName ASC")
    List<Doctor> findDoctorsBySpecialization(@Param("name") String name);
}