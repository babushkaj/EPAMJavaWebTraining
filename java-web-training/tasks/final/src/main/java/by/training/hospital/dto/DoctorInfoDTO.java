package by.training.hospital.dto;

import by.training.hospital.entity.Specialization;

import java.time.DayOfWeek;
import java.util.Objects;
import java.util.Set;

public class DoctorInfoDTO {

    private Long id;
    private Specialization spec;
    private Set<DayOfWeek> workingDays;
    private String description;
    private Long userId;

    public DoctorInfoDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Specialization getSpec() {
        return spec;
    }

    public void setSpec(Specialization spec) {
        this.spec = spec;
    }

    public Set<DayOfWeek> getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(Set<DayOfWeek> workingDays) {
        this.workingDays = workingDays;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoctorInfoDTO that = (DoctorInfoDTO) o;
        return Objects.equals(id, that.id) &&
                spec == that.spec &&
                Objects.equals(workingDays, that.workingDays) &&
                Objects.equals(description, that.description) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, spec, workingDays, description, userId);
    }
}
