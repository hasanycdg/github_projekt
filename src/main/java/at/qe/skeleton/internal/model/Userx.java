package at.qe.skeleton.internal.model;

import  java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import at.qe.skeleton.configs.WebSecurityConfig;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.domain.Persistable;

/**
 * Entity representing users.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
*/
@Entity
public class Userx implements Persistable<String>, Serializable, Comparable<Userx> {

    private static final long serialVersionUID = 1L;
    //the following 3 classes are for the  observations of premium

    @Id
    @Column(length = 100)
    private String username;
    //changed optional to true because of the missing logged in user in the moment of
    //the registration, need to think of the importance of this column in our application

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createDate;
    @ManyToOne(optional = true)
    private Userx updateUser;
    @UpdateTimestamp
    private LocalDateTime updateDate;

    private String password;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    boolean premium;

    boolean enabled;

    @ElementCollection(targetClass = UserxRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "Userx_UserxRole")
    @Enumerated(EnumType.STRING)
    private Set<UserxRole> roles;

    @OneToMany(mappedBy="user", cascade=CascadeType.REMOVE)
    private List<FavLocation> favoriteLocations = new ArrayList<>();
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true) //user deletion results in deletion of credit card
    @JoinColumn(name = "credit_card_id")
    private CreditCard creditCard;

    @OneToMany(mappedBy = "user" , cascade = CascadeType.REMOVE) // Die mappedBy-Annotation bezieht sich auf das Attribut "user" in der PremiumHistory-Klasse
    private List<PremiumHistory> premiumHistoryList = new ArrayList<>();

    @OneToMany(mappedBy ="user" , cascade = CascadeType.REMOVE)
    private List<PaymentHistory> paymentHistoryList = new ArrayList<>();

    @OneToMany(mappedBy ="user" , cascade = CascadeType.REMOVE)
    private List<RolChangeLog> rolChangeLogs = new ArrayList<>();

    public List<RolChangeLog> getRolChangeLogs() {
        return rolChangeLogs;
    }

    public Userx setRolChangeLogs(List<RolChangeLog> rolChangeLogs) {
        this.rolChangeLogs = rolChangeLogs;
        return this;
    }

    public List<PremiumHistory> getPremiumHistoryList() {
        return premiumHistoryList;
    }

    public void setPremiumHistoryList(List<PremiumHistory> premiumHistoryList) {
        this.premiumHistoryList = premiumHistoryList;
    }

    public List<PaymentHistory> getPaymentHistoryList() {
        return paymentHistoryList;
    }

    public void setPaymentHistoryList(List<PaymentHistory> paymentHistoryList) {
        this.paymentHistoryList = paymentHistoryList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = WebSecurityConfig.passwordEncoder().encode(password);
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<UserxRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserxRole> roles) {
        this.roles = roles;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public Userx getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Userx updateUser) {
        this.updateUser = updateUser;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public List<FavLocation> getLocations() {
        return favoriteLocations;
    }

    public void setLocations(List<FavLocation> locations) {
        this.favoriteLocations = locations;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.username);
        return hash;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Userx)) {
            return false;
        }
        final Userx other = (Userx) obj;
        return Objects.equals(this.username, other.username);
    }

    @Override
    public String toString() {
        return "at.qe.skeleton.model.User[ id=" + username + " ]";
    }

    @Override
    public String getId() {
        return getUsername();
    }

    public void setId(String id) {
        setUsername(id);
    }

    @Override
    public boolean isNew() {
        return (null == createDate);
    }

    @Override
    public int compareTo(Userx o) {
        return this.username.compareTo(o.getUsername());
    }

}
