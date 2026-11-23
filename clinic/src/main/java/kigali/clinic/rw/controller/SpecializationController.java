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

import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.service.SpecializationService;

@RestController
@RequestMapping(value = "/api/specialization")
public class SpecializationController {

    @Autowired
    private SpecializationService specializationServe;

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveSpecialization(@RequestBody Specialization specialization) {

        String returnedMessage = specializationServe.saveSpecialization(specialization);

        if (returnedMessage.equals("Specialization is saved successfully")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllSpecializations() {

        return new ResponseEntity<>(specializationServe.getAllSpecializations(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getSpecializationById(@PathVariable UUID id) {

        Optional<Specialization> specialization = specializationServe.getSpecializationById(id);

        if (specialization.isPresent()) {
            return new ResponseEntity<>(specialization.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Specialization with id " + id + " not found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updateSpecialization(@PathVariable UUID id, @RequestBody Specialization specialization) {

        String returnedMessage = specializationServe.updateSpecialization(id, specialization);

        if (returnedMessage.equals("Specialization is updated successfully")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteSpecialization(@PathVariable UUID id) {

        String returnedMessage = specializationServe.deleteSpecialization(id);

        if (returnedMessage.equals("Specialization is deleted successfully")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
    }

    // Many-to-Many relationship: assign / remove a doctor for this specialization.

    @PutMapping(value = "/{specializationId}/doctor/{doctorId}")
    public ResponseEntity<?> addDoctorToSpecialization(@PathVariable UUID specializationId, @PathVariable UUID doctorId) {

        String returnedMessage = specializationServe.addDoctorToSpecialization(specializationId, doctorId);

        if (returnedMessage.equals("Doctor is assigned to specialization successfully")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/{specializationId}/doctor/{doctorId}")
    public ResponseEntity<?> removeDoctorFromSpecialization(@PathVariable UUID specializationId, @PathVariable UUID doctorId) {

        String returnedMessage = specializationServe.removeDoctorFromSpecialization(specializationId, doctorId);

        if (returnedMessage.equals("Doctor is removed from specialization successfully")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
    }
}
