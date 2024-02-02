package at.qe.skeleton.tests;

import at.qe.skeleton.external.model.currentandforecast.misc.AlertDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AlertDTOTest {

    @Test
    public void testConstructor() {
        AlertDTO alert = new AlertDTO("senderName", "event", Instant.parse("2024-01-20T21:00:00Z"), Instant.parse("2024-01-21T01:00:00Z"), "description", List.of("severe weather"));
        Assertions.assertEquals("senderName", alert.senderName());
        Assertions.assertEquals("event", alert.event());
        Assertions.assertEquals(Instant.parse("2024-01-20T21:00:00Z"), alert.start());
        Assertions.assertEquals(Instant.parse("2024-01-21T01:00:00Z"), alert.end());
        Assertions.assertEquals("description", alert.description());
        Assertions.assertEquals(List.of("severe weather"), alert.tags());
    }
    @Test
    public void testConstructorWithNullValues() {
        AlertDTO alert = new AlertDTO(null, null, null, null, null, null);
        Assertions.assertNull(alert.senderName());
        Assertions.assertNull(alert.event());
        Assertions.assertNull(alert.start());
        Assertions.assertNull(alert.end());
        Assertions.assertNull(alert.description());
        Assertions.assertNull(alert.tags());
    }

    @Test
    public void testConstructorWithInvalidData() {
        // Assuming Instant.parse would throw DateTimeParseException for invalid formats
        Assertions.assertThrows(DateTimeParseException.class, () -> {
            AlertDTO alert = new AlertDTO("senderName", "event", Instant.parse("invalid date"), Instant.parse("invalid date"), "description", List.of("severe weather"));
        });
    }
}
