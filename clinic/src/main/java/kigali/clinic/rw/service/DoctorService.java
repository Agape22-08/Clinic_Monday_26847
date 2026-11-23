package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.OfficeRepository;
import kigali.clinic.rw.repository.SpecializationRepository;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private SpecializationRepository specializationRepository;

    @Autowired
    private OfficeRepository officeRepository;

    public String saveDoctor(Doctor doctor) {

        doctorRepo.save(doctor);

        return "Doctor is saved successfully";
    }

    public List<Doctor> getAllDoctors() {

        return doctorRepo.findAll();
    }

    public Optional<Doctor> getDoctorById(UUID id) {

        return doctorRepo.findById(id);
    }

    public String updateDoctor(UUID id, Doctor doctorDetails) {

        Optional<Doctor> existingDoctor = doctorRepo.findById(id);

        if (existingDoctor.isEmpty()) {
            return "Doctor with id " + id + " not found";
        }

        Doctor doctor = existingDoctor.get();
        doctor.setFirstName(doctorDetails.getFirstName());
        doctor.setLastName(doctorDetails.getLastName());
        doctor.setDateOfBirth(doctorDetails.getDateOfBirth());

        doctorRepo.save(doctor);

        return "Doctor is updated successfully";
    }

    public String deleteDoctor(UUID id) {

        if (!doctorRepo.existsById(id)) {
            return "Doctor with id " + id + " not found";
        }

        doctorRepo.deleteById(id);

        return "Doctor is deleted successfully";
    }

    public String addSpecializationToDoctor(UUID doctorId, UUID specId) {
        Optional<Doctor> optDoctor = doctorRepo.findById(doctorId);
        if (optDoctor.isEmpty()) {
            return "The doctor with that id does not exist";
        }
        Optional<Specialization> optSpec = specializationRepository.findById(specId);
        if (optSpec.isEmpty()) {
            return "That specialization does not exist";
        }
        Doctor doctor = optDoctor.get();
        Specialization spec = optSpec.get();

        if (doctor.getSpecializations().contains(spec)) {
            return "The doctor already has that specialization";
        }
        doctor.getSpecializations().add(spec);
        doctorRepo.save(doctor);
        return "Specialization added to the doctor";
    }

    public String removeSpecializationFromDoctor(UUID doctorId, UUID specId) {
        Optional<Doctor> optDoctor = doctorRepo.findById(doctorId);
        if (optDoctor.isEmpty()) {
            return "The doctor with that id does not exist";
        }
        Optional<Specialization> optSpec = specializationRepository.findById(specId);
        if (optSpec.isEmpty()) {
            return "That specialization does not exist";
        }
        Doctor doctor = optDoctor.get();
        Specialization spec = optSpec.get();

        if (!doctor.getSpecializations().contains(spec)) {
            return "The doctor does not have that specialization";
        }
        doctor.getSpecializations().remove(spec);
        doctorRepo.save(doctor);
        return "Specialization removed from the doctor";
    }

    public String moveDoctorToOffice(UUID doctorId, int officeNumber) {
        Optional<Doctor> optDoctor = doctorRepo.findById(doctorId);
        if (optDoctor.isEmpty()) {
            return "The doctor with that id does not exist";
        }
        Optional<Office> optOffice = officeRepository.findByOfficeNumber(officeNumber);
        if (optOffice.isEmpty()) {
            return "The office with that office number does not exist";
        }
        Office newOffice = optOffice.get();
        Doctor doctor = optDoctor.get();

        // check if office is taken by OTHER doctor
        Optional<Doctor> doctorInThatOffice = doctorRepo.findByOffice(newOffice);
        if (doctorInThatOffice.isPresent() && !doctorInThatOffice.get().getId().equals(doctorId)) {
            return "That office is taken by other doctor";
        }
        doctor.setOffice(newOffice);
        doctorRepo.save(doctor);
        return "Office changed successfully";
    }
}
