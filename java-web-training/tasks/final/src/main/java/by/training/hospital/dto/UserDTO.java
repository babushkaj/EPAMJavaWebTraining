package by.training.hospital.dto;

import by.training.hospital.entity.Role;

import java.util.List;
import java.util.Objects;

public class UserDTO {
    private Long id;
    private String login;
    private String password;
    private Role role;
    private boolean blocked;

    private UserInfoDTO userInfo;
    private AddressDTO address;

    private DoctorInfoDTO doctorInfo;

    private List<VisitDTO> visits;

    public UserDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public UserInfoDTO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoDTO userInfo) {
        this.userInfo = userInfo;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    public DoctorInfoDTO getDoctorInfo() {
        return doctorInfo;
    }

    public void setDoctorInfo(DoctorInfoDTO doctorInfo) {
        this.doctorInfo = doctorInfo;
    }

    public List<VisitDTO> getVisits() {
        return visits;
    }

    public void setVisits(List<VisitDTO> visits) {
        this.visits = visits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return blocked == userDTO.blocked &&
                Objects.equals(id, userDTO.id) &&
                Objects.equals(login, userDTO.login) &&
                Objects.equals(password, userDTO.password) &&
                role == userDTO.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, role, blocked);
    }
}
