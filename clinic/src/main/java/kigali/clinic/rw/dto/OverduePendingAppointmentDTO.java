package kigali.clinic.rw.dto;

import java.sql.Date;

/**
 * DTO for Exercise 4, query 2:
 * PENDING appointments whose date has passed, with patient name, doctor name and date.
 */
public class OverduePendingAppointmentDTO {

    private String patientName;
    private String doctorName;
    private Date appointmentDate;

    public OverduePendingAppointmentDTO(String patientName, String doctorName, Date appointmentDate) {
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.appointmentDate = appointmentDate;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }
}
