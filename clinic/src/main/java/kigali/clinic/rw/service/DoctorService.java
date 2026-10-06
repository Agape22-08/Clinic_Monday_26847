package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.repository.DoctorRepository;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepo;

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
}
