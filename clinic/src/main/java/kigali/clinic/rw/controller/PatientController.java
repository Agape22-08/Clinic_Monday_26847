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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.service.AppointmentService;
import kigali.clinic.rw.service.PatientService;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(value = "/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientServe;

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping(value = "/save")
    public ResponseEntity<?> savePatient(@RequestBody Patient patient) {

        String returnedMessage = patientServe.savePatient(patient);

        if (returnedMessage.equals("Patient is saved successfully")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllPatients() {

        return new ResponseEntity<>(patientServe.getAllPatients(), HttpStatus.OK);
    }
    @GetMapping(value = "/by-last-name")
public ResponseEntity<?> getPatientsByLastName(@RequestParam String lastName) {

    return new ResponseEntity<>(
            patientServe.getPatientsByLastName(lastName),
            HttpStatus.OK
    );
}

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable UUID id) {

        Optional<Patient> patient = patientServe.getPatientById(id);

        if (patient.isPresent()) {
            return new ResponseEntity<>(patient.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Patient with id " + id + " not found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable UUID id, @RequestBody Patient patient) {

        String returnedMessage = patientServe.updatePatient(id, patient);

        if (returnedMessage.equals("Patient is updated successfully")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable UUID id) {

        String returnedMessage = patientServe.deletePatient(id);

        if (returnedMessage.equals("Patient is deleted successfully")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
    }

    // D2
    @GetMapping("/{ssNumber}/upcoming")
    public ResponseEntity<?> getUpcoming(@PathVariable String ssNumber) {
        return ResponseEntity.ok(appointmentService.getUpcomingAppointments(ssNumber));
    }
}
