package kigali.clinic.rw.repository;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    List<Appointment> findByStatusOrderByAppointmentDateAsc(AppointmentStatus status);

    List<Appointment> findByAppointmentDateBetweenOrderByAppointmentDateAsc(
            Date start,
            Date end
    );


    boolean existsByDoctorIdAndAppointmentDateAndStatusNot(
            UUID doctorId,
            Date appointmentDate,
            AppointmentStatus status
    );

   
    @Query("""
        SELECT d
        FROM Doctor d
        JOIN d.specializations s
        WHERE LOWER(s.name) = LOWER(:name)
        """)
    List<kigali.clinic.rw.domain.Doctor> findDoctorsBySpecialization(
            @Param("name") String name
    );


    @Query("""
        SELECT d
        FROM Doctor d
        WHERE d.office IS NULL
        ORDER BY d.lastName ASC
        """)
    List<kigali.clinic.rw.domain.Doctor> findDoctorsWithoutOffice();

    
    @Query("""
        SELECT DISTINCT a.patient
        FROM Appointment a
        WHERE a.doctor.id = :doctorId
        """)
    List<kigali.clinic.rw.domain.Patient> findPatientsOfDoctor(
            @Param("doctorId") UUID doctorId
    );

   
    @Query("""
        SELECT a.status, COUNT(a)
        FROM Appointment a
        GROUP BY a.status
        """)
    List<Object[]> countAppointmentsByStatus();

    
    @Query("""
        SELECT a.patient
        FROM Appointment a
        GROUP BY a.patient
        HAVING COUNT(a) >= :min
        ORDER BY COUNT(a) DESC
        """)
    List<kigali.clinic.rw.domain.Patient> findFrequentPatients(
            @Param("min") long min
    );

   
    @Query("""
        SELECT o.name, o.officeNumber, COUNT(a)
        FROM Office o
        JOIN o.doctor d
        JOIN d.appointments a
        GROUP BY o.id, o.name, o.officeNumber
        ORDER BY COUNT(a) DESC
        """)
    List<Object[]> findBusiestOffice();

   
    @Modifying
    @Transactional
    @Query("""
        UPDATE Appointment a
        SET a.status = kigali.clinic.rw.domain.AppointmentStatus.CANCELLED
        WHERE a.doctor.id = :doctorId
        AND a.appointmentDate = :date
        AND a.status <> kigali.clinic.rw.domain.AppointmentStatus.COMPLETED
        """)
    int cancelAppointmentsForDoctorOnDate(
            @Param("doctorId") UUID doctorId,
            @Param("date") Date date
    );

    Page<Appointment> findAll(Pageable pageable);

    
    @Modifying
    @Transactional
    @Query("""
        DELETE FROM Appointment a
        WHERE a.status = kigali.clinic.rw.domain.AppointmentStatus.CANCELLED
        AND a.appointmentDate < :date
        """)
    int deleteCancelledBefore(
            @Param("date") Date date
    );
}