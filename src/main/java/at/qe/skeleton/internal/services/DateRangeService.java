package at.qe.skeleton.internal.services;


import org.springframework.stereotype.Component;
import java.time.LocalDate;
/**
 * Service class for handling operations related to date ranges.
 * This class provides functionalities such as validating date ranges and calculating the midpoint of a date range.
 */

@Component
public class DateRangeService {

    /**
     * Checks if the provided date range is valid. A valid date range is where the start date is not after the end date
     * and the range is not longer than 14 days.
     *
     *  startDate the start date of the range
     *  endDate   the end date of the range
     * @return true if the date range is valid, false otherwise
     */

    public static boolean isDateRangeValid(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return false;
        }
        if (startDate.isAfter(endDate)) {
            return false;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) <= 14;
    }
    /**
     * Calculates the midpoint date of a given date range. If the start or end date is null,
     * or if the start date is after the end date, this method returns null.
     *
     *  startDate the start date of the range
     *  endDate   the end date of the range
     * @return the midpoint date of the range, or null if inputs are invalid
     */

    public static LocalDate calculateMidpointDate(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return null;
        }
        int daysBetween = (int) java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
        return startDate.plusDays(daysBetween / 2);
    }
}
