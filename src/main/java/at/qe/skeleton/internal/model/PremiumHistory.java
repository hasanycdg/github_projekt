package at.qe.skeleton.internal.model;


import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entity Representing the Premium History. The Premium History saves every change in the Premium Status
 * of a User.
 */
@Entity
public class PremiumHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Userx user;

    @Column(nullable = false)
    private LocalDateTime changeDate;

    @Column(nullable = false)
    private boolean newPremiumStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Userx getUser() {
        return user;
    }

    public void setUser(Userx user) {
        this.user = user;
    }

    public LocalDateTime getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(LocalDateTime changeDate) {
        this.changeDate = changeDate;
    }

    public boolean getNewPremiumStatus() {
        return newPremiumStatus;
    }

    public void setNewPremiumStatus(boolean newPremiumStatus) {
        this.newPremiumStatus = newPremiumStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PremiumHistory that = (PremiumHistory) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "|ChangeDate: " + changeDate + " new Status: " + newPremiumStatus + "|";
    }
}


