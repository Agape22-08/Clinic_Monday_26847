package kigali.clinic.rw.controller;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.service.AppointmentService;
import kigali.clinic.rw.service.DoctorService;

@RestController
@RequestMapping(value = "/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorServe;

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveDoctor(@RequestBody Doctor doctor) {

        String returnedMessage = doctorServe.saveDoctor(doctor);

        if (returnedMessage.equals("Doctor is saved successfully")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllDoctors() {

        return new ResponseEntity<>(doctorServe.getAllDoctors(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable UUID id) {

        Optional<Doctor> doctor = doctorServe.getDoctorById(id);

        if (doctor.isPresent()) {
            return new ResponseEntity<>(doctor.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Doctor with id " + id + " not found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updateDoctor(@PathVariable UUID id, @RequestBody Doctor doctor) {

        String returnedMessage = doctorServe.updateDoctor(id, doctor);

        if (returnedMessage.equals("Doctor is updated successfully")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable UUID id) {

        String returnedMessage = doctorServe.deleteDoctor(id);

        if (returnedMessage.equals("Doctor is deleted successfully")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{doctorId}/specializations/{specializationId}")
    public ResponseEntity<String> addSpecialization(@PathVariable UUID doctorId, @PathVariable UUID specializationId) {
        String msg = doctorServe.addSpecializationToDoctor(doctorId, specializationId);
        if (msg.equals("The doctor with that id does not exist") || msg.equals("That specialization does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        if (msg.equals("The doctor already has that specialization")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
        }
        return ResponseEntity.ok(msg);
    }

    @DeleteMapping("/{doctorId}/specializations/{specializationId}")
    public ResponseEntity<String> removeSpecialization(@PathVariable UUID doctorId, @PathVariable UUID specializationId) {
        String msg = doctorServe.removeSpecializationFromDoctor(doctorId, specializationId);
        if (msg.equals("The doctor with that id does not exist") || msg.equals("That specialization does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        if (msg.equals("The doctor does not have that specialization")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
        }
        return ResponseEntity.ok(msg);
    }

    @PatchMapping("/{doctorId}/office")
    public ResponseEntity<String> moveOffice(@PathVariable UUID doctorId, @RequestParam int officeNumber) {
        String msg = doctorServe.moveDoctorToOffice(doctorId, officeNumber);
        if (msg.equals("The doctor with that id does not exist") || msg.equals("The office with that office number does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        if (msg.equals("That office is taken by other doctor")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
        }
        return ResponseEntity.ok(msg);
    }

    // D1
    @GetMapping("/{doctorId}/agenda")
    public ResponseEntity<?> getDoctorAgenda(@PathVariable UUID doctorId, @RequestParam String date) {
        return ResponseEntity.ok(appointmentService.getDoctorAgenda(doctorId, Date.valueOf(date)));
    }

    // D3
    @GetMapping("/search")
    public ResponseEntity<?> searchDoctors(@RequestParam String name) {
        return ResponseEntity.ok(appointmentService.searchDoctorsByName(name));
    }
}
