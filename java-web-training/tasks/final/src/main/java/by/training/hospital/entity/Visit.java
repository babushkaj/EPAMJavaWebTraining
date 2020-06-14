package by.training.hospital.entity;

import java.util.Objects;

public class Visit {

    private Long id;
    private String cause;
    private String result;
    private Long date;
    private VisitStatus visitStatus;

    private Long visitorId;
    private Long doctorId;

    public Visit() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public VisitStatus getVisitStatus() {
        return visitStatus;
    }

    public void setVisitStatus(VisitStatus visitStatus) {
        this.visitStatus = visitStatus;
    }

    public Long getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(Long visitorId) {
        this.visitorId = visitorId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Visit visit = (Visit) o;
        return Objects.equals(id, visit.id) &&
                Objects.equals(cause, visit.cause) &&
                Objects.equals(result, visit.result) &&
                Objects.equals(date, visit.date) &&
                visitStatus == visit.visitStatus &&
                Objects.equals(visitorId, visit.visitorId) &&
                Objects.equals(doctorId, visit.doctorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cause, result, date, visitStatus, visitorId, doctorId);
    }
}
