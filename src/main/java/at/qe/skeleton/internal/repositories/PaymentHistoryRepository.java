package at.qe.skeleton.internal.repositories;

import at.qe.skeleton.internal.model.PaymentHistory;
import at.qe.skeleton.internal.model.Userx;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for managing {@link PaymentHistory} entities.
 *
 */
public interface PaymentHistoryRepository extends AbstractRepository <PaymentHistory, Long>, Serializable {

    public PaymentHistory findByUser (Userx user);

    @Query("Select ph FROM PaymentHistory ph WHERE YEAR(ph.changeDate) = :paymentYear AND MONTH (ph.changeDate) = :paymentMonth")
    List<PaymentHistory> findByChangeDate(
            @Param("paymentYear") int paymentYear,
            @Param("paymentMonth") int paymentMonth
    );


    //count > 0 evaluates to true if the value is greater than 0.
    @Query("SELECT COUNT(ph) > 0 FROM PaymentHistory ph WHERE ph.user = :user AND YEAR(ph.changeDate) = :paymentYear AND MONTH(ph.changeDate) = :paymentMonth")
    boolean exitsByUserAndPaymentYearAndPaymentMonth(
            @Param("user") Userx user,
            @Param("paymentYear") int paymentYear,
            @Param("paymentMonth") int paymentMonth
    );

    @Query("SELECT ph FROM PaymentHistory ph WHERE ph.user = :user AND YEAR(ph.changeDate) = :paymentYear AND MONTH(ph.changeDate) = :paymentMonth")
    PaymentHistory findByUserAndPaymentYearAndPaymentMonth(
            @Param("user") Userx user,
            @Param("paymentYear") int paymentYear,
            @Param("paymentMonth") int paymentMonth
    );

    public List<PaymentHistory> findAllByChangeDateBeforeAndUser(LocalDateTime changeDate, Userx userx);
}
