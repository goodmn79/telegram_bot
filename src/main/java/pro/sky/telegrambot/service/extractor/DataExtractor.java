package pro.sky.telegrambot.service.extractor;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DataExtractor {
    public static final String TIME_FORMAT = "HH:mm";
    public static final String DATE_TIME_FORMAT = String.format("dd.MM.yyyy %s", TIME_FORMAT);

    private static final String DATE_REGEX = "\\b(\\d{2}\\.\\d{2}\\.\\d{4})\\b";
    private static final String TIME_REGEX = "\\b(\\d{2}:\\d{2})\\b";

    private final LocalDate currentDate = LocalDate.now();


    public LocalDateTime extractDateTime(String text) {
        String dateTime = dateTimeString(text);
        if (dateTime.isEmpty()) {
            return LocalDateTime.MIN;
        }
        DateTimeFormatter formatter = dateTime.contains(" ")
                ? DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)
                : DateTimeFormatter.ofPattern(TIME_FORMAT);

        if (dateTime.length() == TIME_FORMAT.length()) {
            LocalTime time = LocalTime.parse(dateTime, formatter);
            return LocalDateTime.of(currentDate, time);
        }
        try {
            return LocalDateTime.parse(dateTime, formatter);
        } catch (DateTimeParseException e) {
            return LocalDateTime.MIN;
        }
    }

    public String extractText(String text) {
        String dateTimeRegex = String.format("(%s %s|%s|%s)", DATE_REGEX, TIME_REGEX, DATE_REGEX, TIME_REGEX);
        return text.replaceFirst(dateTimeRegex, "").trim();
    }

    private String dateTimeString(String text) {
        String date = matchedValue(DATE_REGEX, text);
        String time = matchedValue(TIME_REGEX, text);
        if (time == null) return "";
        if (date == null) return time;
        return date + " " + time;
    }

    private String matchedValue(String regex, String text) {
        Pattern datePattern = Pattern.compile(regex);
        Matcher matcher = datePattern.matcher(text);
        return matcher.find() ? matcher.group() : null;
    }
}
