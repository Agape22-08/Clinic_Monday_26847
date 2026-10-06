package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.repository.PatientRepository;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepo;

    public String savePatient(Patient patient) {

        patientRepo.save(patient);

        return "Patient is saved successfully";
    }

    public List<Patient> getAllPatients() {

        return patientRepo.findAll();
    }
    public List<Patient> getPatientsByLastName(String lastName) {
        return patientRepo.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    
    }

    public Optional<Patient> getPatientById(UUID id) {

        return patientRepo.findById(id);
    }

    public String updatePatient(UUID id, Patient patientDetails) {

        Optional<Patient> existingPatient = patientRepo.findById(id);

        if (existingPatient.isEmpty()) {
            return "Patient with id " + id + " not found";
        }

        Patient patient = existingPatient.get();
        patient.setFirstName(patientDetails.getFirstName());
        patient.setLastName(patientDetails.getLastName());
        patient.setDateOfBirth(patientDetails.getDateOfBirth());
        patient.setSocialSecurityNumber(patientDetails.getSocialSecurityNumber());

        patientRepo.save(patient);

        return "Patient is updated successfully";
    }

    public String deletePatient(UUID id) {

        if (!patientRepo.existsById(id)) {
            return "Patient with id " + id + " not found";
        }

        patientRepo.deleteById(id);

        return "Patient is deleted successfully";
    }
}
