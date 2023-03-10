package vn.mobileid.selfcare.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
//import org.hibernate.annotations.BatchSize;
//import org.hibernate.annotations.Cache;
//import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Email;
import vn.mobileid.selfcare.config.Constants;

//import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.time.Instant;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = {"password", "newPassword", "confirmNewPassword"})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
//    @Column(length = 50, unique = true, nullable = false)
    private String login;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
//    @Column( length = 60)
    private String password;

    @Size(max = 50)
//    @Column(length = 50)
    private String firstName;

    @Size(max = 50)
//    @Column( length = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 100)
//    @Column(length = 100, unique = true)
    private String email;

//    @Column(name = "created_date")
    private Instant createdDate = Instant.now();

    @NotNull
//    @Column(nullable = false)
    private boolean activated = false;

    @Size(min = 2, max = 5)
//    @Column(length = 5)
    private String langKey;

    @Size(max = 256)
//    @Column(length = 256)
    private String imageUrl;

    @Size(max = 20)
//    @Column(length = 20)
    @JsonIgnore
    private String activationKey;

    @Size(max = 20)
//    @Column(length = 20)
    @JsonIgnore
    private String resetKey;

//    @Column()
    private Instant resetDate = null;

    @Size(min = 7, max = 15)
//    @Column( length = 15)
    String ipAddress;

    @JsonIgnore
//    @ManyToMany
//    @JoinTable(name = "app_user_authority", joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "authority_name", referencedColumnName = "name") })
//    @BatchSize(size = 20)
    private Set<Authority> authorities = new HashSet<>();


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
        this.login = login.toLowerCase(Locale.ENGLISH);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean getActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public Instant getResetDate() {
        return resetDate;
    }

    public void setResetDate(Instant resetDate) {
        this.resetDate = resetDate;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        return login.equals(user.login);
    }

    @Override
    public int hashCode() {
        return login.hashCode();
    }

    @Override
    public String toString() {
        return "User{" + "login='" + login + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", email='" + email + '\'' + ", email='" + createdDate + '\'' + ", imageUrl='" + imageUrl + '\'' + ", activated='" + activated + '\''
                + ", langKey='" + langKey + '\'' + ", activationKey='" + activationKey + '\'' + "}";
    }
}
