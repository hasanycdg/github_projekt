package at.qe.skeleton.internal.services;


import at.qe.skeleton.internal.model.PaymentHistory;
import at.qe.skeleton.internal.model.PaymentStatus;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.repositories.PaymentHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


/**
 * Service for accessing and manipulating Payment History Data.
 *
 */


@Component
@Scope("application")
public class PaymentHistoryService implements Serializable {

    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;


    /**
     * Methode to create a new PaymentHistory. The Methode is only creating a new history if there is
     * no other history for the user in the selected month and year.
     * @param user
     */
    public void createPaymentHistory(Userx user, LocalDateTime yearMonth){

        if(!paymentHistoryRepository.exitsByUserAndPaymentYearAndPaymentMonth(user, yearMonth.getYear(), yearMonth.getMonthValue())) {
            PaymentHistory paymentHistory = new PaymentHistory();
            paymentHistory.setUser(user);
            paymentHistory.setChangeDate(yearMonth);
            paymentHistory.setPaymentStatus(PaymentStatus.OPEN);
            paymentHistoryRepository.save(paymentHistory);
        }
    }

    public void updatePaymentStatus (Userx user, PaymentStatus paymentStatus, Integer chargedDays){
        LocalDateTime now = LocalDateTime.now();
        Integer currentYear = now.getYear();
        Integer currentMonth = now.getMonth().getValue();


        PaymentHistory paymentHistory = paymentHistoryRepository.findByUserAndPaymentYearAndPaymentMonth(user, currentYear, currentMonth);

        if (paymentHistory == null) {
            PaymentHistory newPaymentHistory = new PaymentHistory();
            newPaymentHistory.setUser(user);
            newPaymentHistory.setChangeDate(now);
            newPaymentHistory.setPaymentStatus(paymentStatus);
            newPaymentHistory.setChargedDays(chargedDays);
            paymentHistoryRepository.save(newPaymentHistory);
        } else {
            paymentHistory.setPaymentStatus(paymentStatus);
            paymentHistory.setChargedDays(chargedDays);
            paymentHistoryRepository.save(paymentHistory);
        }
    }

    /**
     * finds and returns a List of all the Payment-History Entries for a certain year and month.
     *
     * @param year
     * @param month
     * @return
     */
    public List<PaymentHistory> getAllByYearAndMonth(Integer year, Integer month) {
        return paymentHistoryRepository.findByChangeDate(year, month);
    }

    /**
     * for test purposes
     * @param changeDate
     * @param userx
     * @return
     */
    public List<PaymentHistory> findAllByChangeDateBeforeAndUser(LocalDateTime changeDate, Userx userx) {
        return paymentHistoryRepository.findAllByChangeDateBeforeAndUser(changeDate, userx);
    }

}
