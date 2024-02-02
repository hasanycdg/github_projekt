package at.qe.skeleton.internal.ui.controllers;


import at.qe.skeleton.internal.model.PaymentStatus;
import at.qe.skeleton.internal.model.PremiumHistory;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.services.CreditCardService;
import at.qe.skeleton.internal.services.PaymentHistoryService;
import at.qe.skeleton.internal.services.PremiumHistoryService;
import at.qe.skeleton.internal.services.UserUpdater;
import at.qe.skeleton.internal.services.email.EmailService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.time.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Component
public class PremiumStatusListener implements PropertyChangeListener, Serializable {

    @Autowired
    private PremiumHistoryService premiumHistoryService;

    @Autowired
    private PaymentHistoryService paymentHistoryService;

    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private UserUpdater userUpdater;

    @Autowired
    private EmailService emailService;


    /**
     * Creates a new PremiumHistory entry when an event from the Observer is triggered.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        if ("premium".equals(evt.getPropertyName())) {
            boolean newPremiumStatus = (boolean) evt.getNewValue();
            Userx user = (Userx) evt.getSource();
            premiumHistoryService.savePremiumHistory(user, newPremiumStatus);
            if (newPremiumStatus) {
                paymentHistoryService.createPaymentHistory(user, LocalDateTime.now());
            }
        }
    }

    /**
     * gets a list of all the dates where a change of the premium status occurred to the user
     *
     * @param user
     * @return
     */
    public List<PremiumHistory> getPremiumIntervalByName(Userx user) {
        return premiumHistoryService.getPremiumChangedByName(user.getUsername());
    }

    /**
     * gives a list of time spans, where the user of allDates was Premium
     * list must be sorted
     *
     * @param allDates
     * @return
     */
    public List<Integer> getTimePremiumInterval(List<PremiumHistory> allDates) {
        List<Duration> intervalls = new ArrayList<>();
        if (allDates.toArray().length < 2) {
            return Collections.emptyList();

        } else if (allDates.toArray().length % 2 == 0) {
            for (int i = 0; i < allDates.toArray().length - 1; i = i + 2) {
                intervalls.add(Duration.between(allDates.get(i).getChangeDate(), allDates.get(i + 1).getChangeDate()));
            }
        } else {
            for (int i = 0; i < allDates.toArray().length - 2; i = i + 2) {
                intervalls.add(Duration.between(allDates.get(i).getChangeDate(), allDates.get(i + 1).getChangeDate()));
            }
        }
        List<Integer> intervallsInInt = new ArrayList<>();
        for (Duration duration : intervalls) {
            intervallsInInt.add((int) duration.toSeconds()); //change to .toDays() //.toSeconds ony for testing purposes
        }
        return intervallsInInt;
    }

    /**
     * gets the total time somebody was premium user by user
     * this is purely a method for display purposes
     *
     * @param user
     * @return
     */
    public Integer getTotalPremiumTimeByName(Userx user) {
        List<Integer> premiumTupelList = getTimePremiumInterval(getPremiumIntervalByName(user));
        if (premiumTupelList.isEmpty()) {
            return 0;
        }
        return premiumTupelList.stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * gets all ChangeDates by user, month and year
     *
     * @param user
     * @param year
     * @param month
     * @return
     */
    public List<PremiumHistory> filterDatesByMonthAndYear(Userx user, int year, Month month) {
        List<PremiumHistory> allDates = getPremiumIntervalByName(user);
        List<PremiumHistory> filteredDates = new ArrayList<>();

        for (PremiumHistory history : allDates) {
            if (history.getChangeDate().getYear() == year && history.getChangeDate().getMonth() == month) {
                filteredDates.add(history);
            }
        }
        return filteredDates;
    }

    /**
     * gives the sum of charged days for the time span given in the invoiceList until end of current month
     *
     * @param invoiceList
     * @return
     */
    public int chargedDaysFromStartToEndCurrentMonth(List<PremiumHistory> invoiceList) {
        if (!invoiceList.get(0).getNewPremiumStatus()) { //was premium from previous Month
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime firstDayOfMonth = currentDateTime.withDayOfMonth(1);
            firstDayOfMonth = firstDayOfMonth.with(LocalTime.MIDNIGHT);

            PremiumHistory dummyStartPoint = new PremiumHistory();
            dummyStartPoint.setNewPremiumStatus(true);
            dummyStartPoint.setChangeDate(firstDayOfMonth);
            invoiceList.add(0, dummyStartPoint);
        }
        if (invoiceList.get(invoiceList.toArray().length - 1).getNewPremiumStatus()) { //still is Premium Users
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime lastDayOfMonth = currentDateTime.withDayOfMonth(currentDateTime.getMonth().length(YearMonth.from(currentDateTime).isLeapYear()));
            lastDayOfMonth = LocalDateTime.of(lastDayOfMonth.toLocalDate(), LocalTime.of(23, 59, 59));

            PremiumHistory dummyEndPoint = new PremiumHistory();
            dummyEndPoint.setNewPremiumStatus(false);
            dummyEndPoint.setChangeDate(lastDayOfMonth);
            invoiceList.add(dummyEndPoint);
        }
        List<Integer> totalTimeTillNow = getTimePremiumInterval(invoiceList);
        if (totalTimeTillNow.isEmpty()) {
            return 0;
        }
        return totalTimeTillNow.stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * calculated the price for chargedDays and updated the DB PaymentHistory
     *
     * @param chargedDays
     * @param user
     * @return
     */
    public double priceForChargedDays(int chargedDays) {
        double pricePerTimeUnit = 0.005; //Time unit is currently a Second
        return chargedDays * pricePerTimeUnit;
    }

    /**
     * does the payment until the end of the current month. of account is to empty it sets premium to false and sends a cancellation e-mail.
     * It also updates the PaymentHistory Data Base
     *
     * @param user
     */
    //Not finished waiting for account and e-mail messaging service
    public void cashUpTillEndCurrentMonth(Userx user) {

        int chargedDays = chargedDaysFromStartToEndCurrentMonth(filterDatesByMonthAndYear(user, LocalDate.now().getYear(), LocalDate.now().getMonth()));
        double payment = priceForChargedDays(chargedDays);
        double currentBalance = creditCardService.findByUser(user).getBalance();

        if(currentBalance >= payment){
            paymentHistoryService.updatePaymentStatus(user, PaymentStatus.PAYED, chargedDays);
            if (user.isPremium()) {
                LocalDateTime currentDateTime = LocalDateTime.now();
                LocalDateTime nextMonth = currentDateTime.plusMonths(1)
                        .withDayOfMonth(1)
                        .withHour(0)
                        .withMinute(0)
                        .withSecond(1);
                paymentHistoryService.createPaymentHistory(user, nextMonth);
            }
        }else {

                paymentHistoryService.updatePaymentStatus(user, PaymentStatus.FAILED, chargedDays);
                user.setPremium(false);
                userUpdater.updateUser(user);
                if (user.getEmail() == null){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("E-Mail not found"));
                return;
                }
                String subject = "Payment Failure";
                String content = "We have to inform you that you monthly payment did not work. Therefore we" +
                        "cancelled your Premium Subscription. Please contact alex.hemmen@student.uibk.ac.at for further Information.";
                emailService.sendSimpleMail(user.getEmail(), subject, content);
        }
    }
}
