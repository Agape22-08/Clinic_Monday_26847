package kigali.clinic.rw.controller;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.service.AppointmentService;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.service.PatientService;
@RestController
@RequestMapping("/api")
public class AppointmentQueryController {

    @Autowired
    private AppointmentService appointmentServe;
    @Autowired
    private PatientService patientServe;

    @Autowired
    private DoctorRepository doctorRepo;


   @GetMapping("/patients/by-last-name")
public ResponseEntity<?> getPatientsByLastName(
        @RequestParam String lastName) {

    return new ResponseEntity<>(
            patientServe.getPatientsByLastName(lastName),
            HttpStatus.OK
    );
}


    @GetMapping("/appointments/by-status")
    public ResponseEntity<?> getAppointmentsByStatus(
            @RequestParam AppointmentStatus status) {

        return new ResponseEntity<>(
                appointmentServe.getAppointmentsByStatus(status),
                HttpStatus.OK
        );
    }


    @GetMapping("/appointments/between")
    public ResponseEntity<?> getAppointmentsBetween(
            @RequestParam String start,
            @RequestParam String end) {

        Date startDate = Date.valueOf(start);
        Date endDate = Date.valueOf(end);

        return new ResponseEntity<>(
                appointmentServe.getAppointmentsBetween(startDate, endDate),
                HttpStatus.OK
        );
    }


    @PostMapping("/appointments/save")
    public ResponseEntity<?> saveAppointment(
            @RequestBody Appointment appointment) {

        String message = appointmentServe.saveAppointment(appointment);

        if (message.equals("Doctor is already booked on that date")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(message, HttpStatus.OK);
    }


    @GetMapping("/doctors/by-specialization")
    public ResponseEntity<?> getDoctorsBySpecialization(
            @RequestParam String name) {

        return new ResponseEntity<>(
                appointmentServe.getDoctorsBySpecialization(name),
                HttpStatus.OK
        );
    }


    @GetMapping("/doctors/without-office")
    public ResponseEntity<?> getDoctorsWithoutOffice() {

        return new ResponseEntity<>(
                appointmentServe.getDoctorsWithoutOffice(),
                HttpStatus.OK
        );
    }


    @GetMapping("/specializations/unused")
    public ResponseEntity<?> getUnusedSpecializations() {

        return new ResponseEntity<>(
                appointmentServe.getUnusedSpecializations(),
                HttpStatus.OK
        );
    }


    @GetMapping("/patients/of-doctor/{doctorId}")
    public ResponseEntity<?> getPatientsOfDoctor(
            @PathVariable UUID doctorId) {

        Optional<Doctor> doctor = doctorRepo.findById(doctorId);

        if (doctor.isEmpty()) {
            return new ResponseEntity<>(
                    "The doctor with that id does not exist",
                    HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(
                appointmentServe.getPatientsOfDoctor(doctorId),
                HttpStatus.OK
        );
    }


    @GetMapping("/appointments/stats/by-status")
    public ResponseEntity<?> getAppointmentStatsByStatus() {

        return new ResponseEntity<>(
                appointmentServe.getAppointmentStatsByStatus(),
                HttpStatus.OK
        );
    }


    @GetMapping("/patients/frequent")
    public ResponseEntity<?> getFrequentPatients(
            @RequestParam long min) {

        return new ResponseEntity<>(
                appointmentServe.getFrequentPatients(min),
                HttpStatus.OK
        );
    }


    @GetMapping("/offices/busiest")
    public ResponseEntity<?> getBusiestOffice() {

        List<Object[]> result = appointmentServe.getBusiestOffice();

        if (result.isEmpty()) {
            return new ResponseEntity<>(
                    "No appointments yet",
                    HttpStatus.OK
            );
        }

        return new ResponseEntity<>(
                result.get(0),
                HttpStatus.OK
        );
    }


    @PatchMapping("/appointments/cancel-day")
    public ResponseEntity<?> cancelAppointmentsForDoctorOnDate(
            @RequestParam UUID doctorId,
            @RequestParam String date) {

        Date appointmentDate = Date.valueOf(date);

        int count = appointmentServe.cancelAppointmentsForDoctorOnDate(
                doctorId,
                appointmentDate
        );

        return new ResponseEntity<>(
                "Cancelled " + count + " appointment(s)",
                HttpStatus.OK
        );
    }


    @GetMapping("/appointments/page")
    public ResponseEntity<?> getAppointmentsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "appointmentDate,desc") String sort) {

        String[] sortParts = sort.split(",");

        String sortField = sortParts[0];
        String direction = sortParts.length > 1
                ? sortParts[1]
                : "asc";

        Page<Appointment> appointmentPage =
                appointmentServe.getAppointmentsPage(
                        page,
                        size,
                        sortField,
                        direction
                );

        return new ResponseEntity<>(
                appointmentPage,
                HttpStatus.OK
        );
    }


    @DeleteMapping("/appointments/cancelled-before")
    public ResponseEntity<?> deleteCancelledBefore(
            @RequestParam String date) {

        Date appointmentDate = Date.valueOf(date);

        int count =
                appointmentServe.deleteCancelledBefore(appointmentDate);

        return new ResponseEntity<>(
                "Deleted " + count + " appointment(s)",
                HttpStatus.OK
        );
    }
}