package kigali.clinic.rw.service;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.domain.Prescription;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PrescriptionService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    public List<Prescription> findByAppointmentId(UUID appointmentId) {
        return prescriptionRepository.findByAppointmentId(appointmentId);
    }

    public List<Prescription> findByPatientSsNumber(String ssNumber) {
        return prescriptionRepository.findByPatientSsNumber(ssNumber);
    }

    public List<Object[]> findTopMedicines() {
        return prescriptionRepository.findTopMedicines();
    }

    public String save(UUID appointmentId, Prescription p){
        Optional<Appointment> opt = appointmentRepository.findById(appointmentId);
        if(opt.isEmpty()){
            return "The appointment with that id does not exist";
        }
        Appointment app = opt.get();
        if(app.getStatus() != AppointmentStatus.COMPLETED){
            return "Prescriptions are only for COMPLETED appointments";
        }

        p.setId(UUID.randomUUID());
        p.setIssuedDate(LocalDate.now());
        p.setAppointment(app);
        prescriptionRepository.save(p);
        return "Prescription saved successfully";
    }

    public String update(UUID id, Prescription updated){
        Optional<Prescription> opt = prescriptionRepository.findById(id);
        if(opt.isEmpty()){
            return "The prescription with that id does not exist";
        }

        Prescription existing = opt.get();
        existing.setMedicineName(updated.getMedicineName());
        existing.setDosage(updated.getDosage());
        existing.setDurationDays(updated.getDurationDays());

        if(updated.getIssuedDate() != null){
            existing.setIssuedDate(updated.getIssuedDate());
        }

        prescriptionRepository.save(existing);
        return "Prescription updated successfully";
    }

    public String delete(UUID id){
        Optional<Prescription> opt = prescriptionRepository.findById(id);
        if(opt.isEmpty()){
            return "The prescription with that id does not exist";
        }
        prescriptionRepository.deleteById(id);
        return "Prescription deleted successfully";
    }
}