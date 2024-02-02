package at.qe.skeleton.internal.ui.beans;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Utility class for formatting timestamps and temperature.
 */
@Component
public class ApiFormatter {

    private ApiFormatter() {
    }

    public static String getDateFormattedFromTimestamp(Instant timestamp){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, MMM d", Locale.ENGLISH);
        return timestamp.atZone(ZoneId.systemDefault()).toLocalDate().format(formatter);
    }


    public static String getTimeFormattedFromTimestamp(Instant timestamp, int offset) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
        return timestamp.atZone(ZoneId.of("UTC")).plusSeconds(offset).format(formatter);
    }

    public static int roundTemperature(double temperature) {
        return (int) Math.round(temperature);
    }
}
