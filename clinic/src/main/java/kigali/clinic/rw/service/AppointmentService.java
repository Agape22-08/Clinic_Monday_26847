package kigali.clinic.rw.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.PatientRepository;
import kigali.clinic.rw.repository.SpecializationRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private SpecializationRepository specializationRepo;


    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {

        return appointmentRepo.findByStatusOrderByAppointmentDateAsc(status);
    }


    public List<Appointment> getAppointmentsBetween(Date start, Date end) {

        return appointmentRepo.findByAppointmentDateBetweenOrderByAppointmentDateAsc(start, end);
    }


    public String saveAppointment(Appointment appointment) {

        boolean alreadyBooked =
                appointmentRepo.existsByDoctorIdAndAppointmentDateAndStatusNot(
                        appointment.getDoctor().getId(),
                        appointment.getAppointmentDate(),
                        AppointmentStatus.CANCELLED
                );

        if (alreadyBooked) {
            return "Doctor is already booked on that date";
        }

        appointmentRepo.save(appointment);

        return "Appointment is saved successfully";
    }


    public List<Doctor> getDoctorsBySpecialization(String name) {

        return appointmentRepo.findDoctorsBySpecialization(name);
    }


    public List<Doctor> getDoctorsWithoutOffice() {

        return appointmentRepo.findDoctorsWithoutOffice();
    }


    public List<Specialization> getUnusedSpecializations() {

        return specializationRepo.findUnusedSpecializations();
    }


    public List<Patient> getPatientsOfDoctor(UUID doctorId) {

        return appointmentRepo.findPatientsOfDoctor(doctorId);
    }


    public List<Object[]> getAppointmentStatsByStatus() {

        return appointmentRepo.countAppointmentsByStatus();
    }


    public List<Patient> getFrequentPatients(long min) {

        return appointmentRepo.findFrequentPatients(min);
    }


    public List<Object[]> getBusiestOffice() {

        return appointmentRepo.findBusiestOffice();
    }


    public int cancelAppointmentsForDoctorOnDate(UUID doctorId, Date date) {

        return appointmentRepo.cancelAppointmentsForDoctorOnDate(doctorId, date);
    }


    public Page<Appointment> getAppointmentsPage(int page, int size, String sortField, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return appointmentRepo.findAll(pageable);
    }


    public int deleteCancelledBefore(Date date) {

        return appointmentRepo.deleteCancelledBefore(date);
    }


    public Optional<Appointment> getAppointmentById(UUID id) {

        return appointmentRepo.findById(id);
    }


    public List<Appointment> getAllAppointments() {

        return appointmentRepo.findAll();
    }
}