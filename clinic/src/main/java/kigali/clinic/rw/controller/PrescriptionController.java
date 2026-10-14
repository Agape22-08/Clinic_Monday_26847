package kigali.clinic.rw.controller;

import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinic.rw.domain.Prescription;
import kigali.clinic.rw.service.PrescriptionService;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestParam UUID appointmentId, @RequestBody Prescription pres) {
        String msg = prescriptionService.save(appointmentId, pres);
        if (msg.equals("Prescription saved successfully"))
            return ResponseEntity.status(HttpStatus.CREATED).body(msg);
        if (msg.equals("The appointment with that id does not exist"))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<?> byAppointment(@PathVariable UUID appointmentId) {
        return ResponseEntity.ok(prescriptionService.findByAppointmentId(appointmentId));
    }

    @GetMapping("/patient/{ssNumber}")
    public List<Prescription> byPatient(@PathVariable String ssNumber) {
        return prescriptionService.findByPatientSsNumber(ssNumber);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody Prescription pres) {
        String msg = prescriptionService.update(id, pres);
        if (msg.equals("The prescription with that id does not exist"))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        return ResponseEntity.ok(msg);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String msg = prescriptionService.delete(id);
        if (msg.equals("The prescription with that id does not exist"))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/stats/top-medicines")
    public List<Object[]> top() {
        return prescriptionService.findTopMedicines();
    }
}
