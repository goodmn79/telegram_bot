package pro.sky.telegrambot.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static pro.sky.telegrambot.service.extractor.DataExtractor.DATE_TIME_FORMAT;

@Entity
@Table(name = "notification_task")
@Data
@Accessors(chain = true)
public class NotificationTask {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String text;
    @Column(name = "date_time")
    private LocalDateTime localDateTime;
    @Column(name = "chat_id")
    private long chatId;

    public String stringRepresentationDateTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return localDateTime.format(dateTimeFormatter);
    }

}
