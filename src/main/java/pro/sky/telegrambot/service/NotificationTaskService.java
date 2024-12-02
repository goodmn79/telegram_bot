package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationRepository;
import pro.sky.telegrambot.service.extractor.DataExtractor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static pro.sky.telegrambot.constant.NotificationMessage.*;

@Service
@RequiredArgsConstructor
public class NotificationTaskService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationTaskService.class);
    private final NotificationRepository notificationRepository;
    private final DataExtractor dataExtractor;

    public String responseText(Message message) {
        long chatId = message.chat().id();
        String messageText = message.text();
        switch (messageText) {
            case MENU -> {
                return LIST;
            }
            case START -> {
                return MESSAGE(WELCOME, MENU);
            }
            case INFO -> {
                return TEXT_INFO;
            }
            case NOTIFICATIONS -> {
                return getAllByChatId(chatId);
            }
        }
        LocalDateTime localDateTime = dataExtractor.extractDateTime(messageText);
        LOGGER.info("Notification date, time is: '{}'", localDateTime);
        if (localDateTime.isBefore(LocalDateTime.now())) return TEXT_INFO;
        String taskText = dataExtractor.extractText(messageText);
        return createTask(localDateTime, taskText, chatId);
    }

    private String createTask(LocalDateTime taskDateTime, String text, long chatId) {
        notificationRepository.save(
                new NotificationTask()
                        .setLocalDateTime(taskDateTime)
                        .setText(text)
                        .setChatId(chatId));
        return SUCCESSFULLY_CREATED;
    }

    public Collection<NotificationTask> getCurrentTimeTasks() {
        return notificationRepository.findAllWithCurrentDateTime();
    }

    public void deleteNotification(NotificationTask notification) {
        notificationRepository.delete(notification);
    }

    private String getAllByChatId(long chatId) {
        List<NotificationTask> notifications = notificationRepository.findAllByChatId(chatId);
        if (notifications.isEmpty()) return NO_NOTIFICATIONS;
        return tasksList(notifications);
    }

    private String tasksList(List<NotificationTask> notificationTasks) {
        StringBuilder tasksListBuilder = new StringBuilder(stringRepresentation(notificationTasks.get(0)));
        for (int i = 1; i < notificationTasks.size(); i++) {
            String task = "\n" + stringRepresentation(notificationTasks.get(i));
            tasksListBuilder.append(task);
        }
        return tasksListBuilder.toString();
    }

    private String stringRepresentation(NotificationTask notificationTask) {
        return String.format("%s %s", notificationTask.stringRepresentationDateTime(), notificationTask.getText());
    }
}
