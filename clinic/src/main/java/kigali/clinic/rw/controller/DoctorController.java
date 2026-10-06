package kigali.clinic.rw.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.service.DoctorService;

@RestController
@RequestMapping(value = "/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorServe;

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
}
