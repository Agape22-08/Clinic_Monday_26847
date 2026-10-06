package kigali.clinic.rw.dto;

import java.util.UUID;

/**
 * DTO for Exercise 4, query 3:
 * Number of appointments per doctor, busiest first.
 */
public class DoctorAppointmentCountDTO {

    private UUID doctorId;
    private String doctorName;
    private Long appointmentCount;

    public DoctorAppointmentCountDTO(UUID doctorId, String doctorName, Long appointmentCount) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.appointmentCount = appointmentCount;
    }

    public UUID getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public Long getAppointmentCount() {
        return appointmentCount;
    }
}
