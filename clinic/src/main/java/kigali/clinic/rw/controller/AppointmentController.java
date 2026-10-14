package kigali.clinic.rw.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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
import kigali.clinic.rw.repository.PrescriptionRepository;
import kigali.clinic.rw.service.AppointmentService;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Appointment appointment){
        String msg = appointmentService.saveAppointment(appointment);
        if(msg.equals("Appointment saved successfully"))
            return ResponseEntity.status(HttpStatus.CREATED).body(msg);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> changeStatus(@PathVariable UUID id, @RequestParam AppointmentStatus status){
        String msg = appointmentService.updateStatus(id, status);
        if(msg.equals("The appointment with that id does not exist"))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        if(msg.startsWith("Cannot change status"))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        return ResponseEntity.ok(msg);
    }

    @PatchMapping("/{id}/reschedule")
    public ResponseEntity<String> reschedule(@PathVariable UUID id, @RequestParam LocalDate newDate){
        String msg = appointmentService.reschedule(id, newDate);
        if(msg.equals("The appointment with that id does not exist"))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        if(msg.contains("cannot be rescheduled") || msg.contains("cannot be in the past"))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        if(msg.equals("Doctor is already booked on that date"))
            return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    // C4 - delete appointment with prescription check
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id){
        if(appointmentService.getAppointmentById(id).isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The appointment with that id does not exist");
        if(prescriptionRepository.existsByAppointmentId(id))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("That appointment has prescriptions, delete them first");
        appointmentService.getAppointmentById(id).ifPresent(a -> appointmentService.deleteAppointment(id));
        return ResponseEntity.ok("Appointment deleted successfully");
    }
}
