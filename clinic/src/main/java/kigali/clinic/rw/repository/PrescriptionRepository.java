package kigali.clinic.rw.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kigali.clinic.rw.domain.Prescription;

public interface PrescriptionRepository extends JpaRepository<Prescription, UUID> {
    List<Prescription> findByAppointmentId(UUID appointmentId); // C2 DERIVED

    boolean existsByAppointmentId(UUID appointmentId); // C4

    @Query("SELECT p FROM Prescription p WHERE p.appointment.patient.socialSecurityNumber = :ss ORDER BY p.issuedDate DESC")
    List<Prescription> findByPatientSsNumber(@Param("ss") String ss); // C3 JPQL

    
    @Query("SELECT p.medicineName, COUNT(p) FROM Prescription p GROUP BY p.medicineName ORDER BY COUNT(p) DESC")
    List<Object[]> findTopMedicines();
}
