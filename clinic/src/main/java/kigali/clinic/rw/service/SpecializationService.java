package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.SpecializationRepository;

@Service
public class SpecializationService {

    @Autowired
    private SpecializationRepository specializationRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    public String saveSpecialization(Specialization specialization) {

        specializationRepo.save(specialization);

        return "Specialization is saved successfully";
    }

    public List<Specialization> getAllSpecializations() {

        return specializationRepo.findAll();
    }

    public Optional<Specialization> getSpecializationById(UUID id) {

        return specializationRepo.findById(id);
    }

    public String updateSpecialization(UUID id, Specialization specializationDetails) {

        Optional<Specialization> existingSpecialization = specializationRepo.findById(id);

        if (existingSpecialization.isEmpty()) {
            return "Specialization with id " + id + " not found";
        }

        Specialization specialization = existingSpecialization.get();
        specialization.setName(specializationDetails.getName());

        specializationRepo.save(specialization);

        return "Specialization is updated successfully";
    }

    public String deleteSpecialization(UUID id) {

        if (!specializationRepo.existsById(id)) {
            return "Specialization with id " + id + " not found";
        }

        specializationRepo.deleteById(id);

        return "Specialization is deleted successfully";
    }

    // Many-to-Many relationship management (Specialization is the owning side of doctor_specialization).

    public String addDoctorToSpecialization(UUID specializationId, UUID doctorId) {

        Optional<Specialization> existingSpecialization = specializationRepo.findById(specializationId);
        Optional<Doctor> existingDoctor = doctorRepo.findById(doctorId);

        if (existingSpecialization.isEmpty()) {
            return "Specialization with id " + specializationId + " not found";
        }

        if (existingDoctor.isEmpty()) {
            return "Doctor with id " + doctorId + " not found";
        }

        Specialization specialization = existingSpecialization.get();
        Doctor doctor = existingDoctor.get();

        boolean alreadyAssigned = specialization.getDoctors().stream()
                .anyMatch(existing -> existing.getId().equals(doctorId));

        if (!alreadyAssigned) {
            specialization.getDoctors().add(doctor);
            specializationRepo.save(specialization);
        }

        return "Doctor is assigned to specialization successfully";
    }

    public String removeDoctorFromSpecialization(UUID specializationId, UUID doctorId) {

        Optional<Specialization> existingSpecialization = specializationRepo.findById(specializationId);

        if (existingSpecialization.isEmpty()) {
            return "Specialization with id " + specializationId + " not found";
        }

        Specialization specialization = existingSpecialization.get();

        boolean removed = specialization.getDoctors().removeIf(doctor -> doctor.getId().equals(doctorId));

        if (!removed) {
            return "Doctor with id " + doctorId + " is not assigned to this specialization";
        }

        specializationRepo.save(specialization);

        return "Doctor is removed from specialization successfully";
    }
}
