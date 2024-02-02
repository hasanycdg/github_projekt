package at.qe.skeleton.internal.ui.beans;

import org.springframework.context.annotation.Scope;
import jakarta.annotation.ManagedBean;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Bean to give a dropdown menu of Years and Months and save the input until next input.
 *
 */



@Component
@Scope("session")
public class DateSelectionBean implements Serializable {

    private List<Integer> years;
    private List<Month> months;
    private Integer selectedYear;
    private Month selectedMonth;
    private Integer selectedMonthInt;
    static final String PRINTDATES = "/manager/user_list.xhtml";


    /**
     * This class represents a DateSelectionBean, which provides a list of years
     * and months for date selection. The default values are set to the current
     * year and month.
     */
    @PostConstruct
    public void init() {
        years = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear+1; i >= currentYear - 11; i--) {
            years.add(i);
        }

        months = Arrays.asList(Month.JANUARY, Month.FEBRUARY, Month.MARCH, Month.APRIL, Month.MAY,
                Month.JUNE, Month.JULY, Month.AUGUST, Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER, Month.DECEMBER);

        //default starting  values, the current year and month
        selectedYear = currentYear;
        selectedMonth = Month.values()[Calendar.getInstance().get(Calendar.MONTH)];
        selectedMonthInt = selectedMonth.getValue();
    }

    public List<Integer> getYears() {
        return years;
    }

    public Integer getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(Integer selectedYear) {
        this.selectedYear = selectedYear;
    }

    public List<Month> getMonths() {
        return months;
    }

    public Month getSelectedMonth() {
        return selectedMonth;
    }
    //updates the Int value of the month on the go

    public void setSelectedMonth(Month selectedMonth) {
        this.selectedMonth = selectedMonth;
        this.selectedMonthInt = selectedMonth.getValue();
    }

    public Integer getSelectedMonthInt() {
        return selectedMonthInt;
    }

    public void setSelectedMonthInt(Integer selectedMonthInt) {
        this.selectedMonthInt = selectedMonthInt;
    }

    public String printDates() {
        return PRINTDATES;
    }

    /**
     * Method to check if the selected Year and Month are the current Year and Month
     * @return
     */
    public boolean isSelectedDateCurrent() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        Month currentMonth = Month.values()[Calendar.getInstance().get(Calendar.MONTH)];

        return selectedYear.equals(currentYear) && selectedMonth.equals(currentMonth);
    }
}