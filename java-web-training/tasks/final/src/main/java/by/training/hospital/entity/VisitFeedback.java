package by.training.hospital.entity;

import java.util.Objects;

public class VisitFeedback {

    private Long id;
    private String visitorMess;
    private String doctorMess;

    private Long visitId;

    public VisitFeedback() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVisitorMess() {
        return visitorMess;
    }

    public void setVisitorMess(String visitorMess) {
        this.visitorMess = visitorMess;
    }

    public String getDoctorMess() {
        return doctorMess;
    }

    public void setDoctorMess(String doctorMess) {
        this.doctorMess = doctorMess;
    }

    public Long getVisitId() {
        return visitId;
    }

    public void setVisitId(Long visitId) {
        this.visitId = visitId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisitFeedback that = (VisitFeedback) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(visitorMess, that.visitorMess) &&
                Objects.equals(doctorMess, that.doctorMess) &&
                Objects.equals(visitId, that.visitId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, visitorMess, doctorMess, visitId);
    }
}
