package at.qe.skeleton.tests;

import at.qe.skeleton.internal.ui.beans.ApiFormatter;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiFormatterTest {

    @Test
    void getDateFormattedFromTimestamp_ValidTimestamp_ReturnsFormattedDate() {
        // Arrange
        Instant timestamp = Instant.parse("2022-01-01T12:34:56Z");
        String expectedFormattedDate = DateTimeFormatter.ofPattern("E, MMM d", Locale.ENGLISH)
                .format(timestamp.atZone(ZoneId.systemDefault()).toLocalDate());

        // Act
        String result = ApiFormatter.getDateFormattedFromTimestamp(timestamp);

        // Assert
        assertEquals(expectedFormattedDate, result);
    }

    @Test
    void getTimeFormattedFromTimestamp_ValidTimestampAndOffset_ReturnsFormattedTime() {
        // Arrange
        Instant timestamp = Instant.parse("2022-01-01T12:34:56Z");
        int offset = 3600; // 1 hour in seconds
        String expectedFormattedTime = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)
                .format(timestamp.atZone(ZoneId.of("UTC")).plusSeconds(offset));

        // Act
        String result = ApiFormatter.getTimeFormattedFromTimestamp(timestamp, offset);

        // Assert
        assertEquals(expectedFormattedTime, result);
    }

    @Test
    void roundTemperature_RoundsTemperature_ReturnsRoundedValue() {
        // Arrange
        double temperature = 23.456;

        // Act
        int result = ApiFormatter.roundTemperature(temperature);

        // Assert
        assertEquals(23, result);
    }
}
