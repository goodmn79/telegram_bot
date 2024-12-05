package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.Message;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationRepository;
import pro.sky.telegrambot.service.command.*;
import pro.sky.telegrambot.service.extractor.DataExtractor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static pro.sky.telegrambot.service.service_message.NotificationMessage.*;

@Service
@RequiredArgsConstructor
public class NotificationTaskService {
    private final NotificationRepository notificationRepository;
    private final DataExtractor dataExtractor;

    private Map<String, Command> telegramBotCommands;

    @PostConstruct
    public void telegramBotCommandsInit() {
        telegramBotCommands = Map.ofEntries(
                entry(INFO, new Info()),
                entry(MENU, new Menu()),
                entry(NOTIFICATIONS, new Notification(this)),
                entry(START, new Start())
        );
    }

    public String responseText(Message message) {
        long chatId = message.chat().id();
        String messageText = message.text();

        Command command = telegramBotCommands.get(messageText);
        if (command != null) {
            return command.createReplyMessage(message);
        }

        LocalDateTime localDateTime = dataExtractor.extractDateTime(messageText);
        if (localDateTime.isBefore(LocalDateTime.now())) return INFO_TEXT;

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

    public String getAllByChatId(long chatId) {
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
