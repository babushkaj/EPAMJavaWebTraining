package by.training.hospital.dto;

import by.training.hospital.entity.VisitStatus;

import java.util.Date;
import java.util.Objects;

public class VisitDTO {

    private long id;
    private String cause;
    private String result;
    private Date date;
    private VisitStatus visitStatus;
    private long visitorId;
    private long doctorId;

    private String visitorFirstName;
    private String visitorLastName;

    private String doctorFirstName;
    private String doctorLastName;

    private VisitFeedbackDTO feedback;

    public VisitDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public VisitStatus getVisitStatus() {
        return visitStatus;
    }

    public void setVisitStatus(VisitStatus visitStatus) {
        this.visitStatus = visitStatus;
    }

    public long getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(long visitorId) {
        this.visitorId = visitorId;
    }

    public long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(long doctorId) {
        this.doctorId = doctorId;
    }

    public String getVisitorFirstName() {
        return visitorFirstName;
    }

    public void setVisitorFirstName(String visitorFirstName) {
        this.visitorFirstName = visitorFirstName;
    }

    public String getVisitorLastName() {
        return visitorLastName;
    }

    public void setVisitorLastName(String visitorLastName) {
        this.visitorLastName = visitorLastName;
    }

    public String getDoctorFirstName() {
        return doctorFirstName;
    }

    public void setDoctorFirstName(String doctorFirstName) {
        this.doctorFirstName = doctorFirstName;
    }

    public String getDoctorLastName() {
        return doctorLastName;
    }

    public void setDoctorLastName(String doctorLastName) {
        this.doctorLastName = doctorLastName;
    }

    public VisitFeedbackDTO getFeedback() {
        return feedback;
    }

    public void setFeedback(VisitFeedbackDTO feedback) {
        this.feedback = feedback;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisitDTO visitDTO = (VisitDTO) o;
        return id == visitDTO.id &&
                visitorId == visitDTO.visitorId &&
                doctorId == visitDTO.doctorId &&
                Objects.equals(cause, visitDTO.cause) &&
                Objects.equals(result, visitDTO.result) &&
                Objects.equals(date, visitDTO.date) &&
                visitStatus == visitDTO.visitStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cause, result, date, visitStatus, visitorId, doctorId);
    }
}
