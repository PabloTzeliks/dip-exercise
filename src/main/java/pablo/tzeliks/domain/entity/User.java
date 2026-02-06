package pablo.tzeliks.domain.entity;

import pablo.tzeliks.domain.entity.vo.Password;

public class User {

    private final Long id;
    private String email;
    private String Telephone;
    private Password password;

    public User(Long id, String email, String telephone, Password password) {
        this.id = id;
        this.email = email;
        Telephone = telephone;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                ", email='" + email + '\'' +
                ", Telephone='" + Telephone + '\'' +
                '}';
    }
}
