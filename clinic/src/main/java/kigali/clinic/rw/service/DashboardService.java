package kigali.clinic.rw.service;

import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.OfficeRepository;
import kigali.clinic.rw.repository.PatientRepository;
import kigali.clinic.rw.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardService {
    @Autowired private PatientRepository patientRepository;
    @Autowired private DoctorRepository doctorRepository;
    @Autowired private OfficeRepository officeRepository;
    @Autowired private AppointmentRepository appointmentRepository;
    @Autowired private PrescriptionRepository prescriptionRepository;

    public Map<String, Long> summary() {
        Map<String, Long> map = new LinkedHashMap<>();
        map.put("patients", patientRepository.count());
        map.put("doctors", doctorRepository.count());
        map.put("freeOffices", (long) officeRepository.findFreeOffices().size());
        map.put("appointmentsToday", appointmentRepository.countByAppointmentDate(Date.valueOf(LocalDate.now())));
        map.put("scheduled", appointmentRepository.countByStatus(kigali.clinic.rw.domain.AppointmentStatus.SCHEDULED));
        map.put("prescriptions", prescriptionRepository.count());
        return map;
    }
}

