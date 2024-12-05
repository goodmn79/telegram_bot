package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduledSearchService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledSearchService.class);

    private final NotificationTaskService service;
    private final TelegramBot telegramBot;

    @Scheduled(cron = "1 0/1 * * * *")
    public void scheduledSearch() {
        LOGGER.info("Scheduled search");
        service.getCurrentTimeTasks().forEach(task -> {
            telegramBot.execute(new SendMessage(task.getChatId(), task.getText()));
            service.deleteNotification(task);
        });
    }
}
