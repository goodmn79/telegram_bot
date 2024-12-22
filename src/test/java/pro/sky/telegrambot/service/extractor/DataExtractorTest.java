package pro.sky.telegrambot.service.extractor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.ChronoUnit.MINUTES;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DataExtractorTest {
    private DataExtractor dataExtractor = new DataExtractor();
    private String messageText;
    private LocalDateTime dateTime;

    @BeforeEach
    void init() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        dateTime = LocalDateTime.now().plusDays(1).truncatedTo(MINUTES);
        String dateTimeString = dateTime.format(formatter);
        messageText = String.format("%s message text", dateTimeString);
    }

    @Test
    void extractDateTimeTest_whenCorrectData_shouldReturnDateTime() {
        LocalDateTime actual = dataExtractor.extractDateTime(messageText);

        assertEquals(dateTime, actual);
    }

    @Test
    void extractText() {
        String expected = "message text";

        String actual = dataExtractor.extractText(messageText);

        assertEquals(expected, actual);
    }
}