package at.qe.skeleton.internal.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Entity Representing the Payment History. The Payment History saves the monthly executed payment roll.
*/

@Entity
public class PaymentHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Userx user;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    private int chargedDays;

    @Column(nullable = false)
    private LocalDateTime changeDate;

    public Userx getUser() {
        return user;
    }

    public void setUser(Userx user) {
        this.user = user;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public int getChargedDays() {
        return chargedDays;
    }

    public void setChargedDays(int chargedDays) {
        this.chargedDays = chargedDays;
    }

    public LocalDateTime getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(LocalDateTime changeDate) {
        this.changeDate = changeDate;
    }

    private String getMonthAndYear(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM")) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentHistory that = (PaymentHistory) o;
        return Objects.equals(user, that.user) &&
                Objects.equals(getMonthAndYear(changeDate), getMonthAndYear(that.changeDate));
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, changeDate);
    }

    @Override
    public String toString() {
        return "Name: " + user.getUsername() + ", Status: " + paymentStatus + ", ChargedDays: " + chargedDays + ", Date: " + changeDate;
    }
}
