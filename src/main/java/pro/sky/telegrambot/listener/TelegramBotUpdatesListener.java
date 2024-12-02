package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.service.NotificationTaskService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramBotUpdatesListener implements UpdatesListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    @Autowired
    private TelegramBot telegramBot;

    private final NotificationTaskService service;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }


    @Scheduled(cron = "1 0/1 * * * *")
    public void scheduledSearch() {
        LOGGER.info("Scheduled search");
        service.getCurrentTimeTasks().forEach(task -> {
            telegramBot.execute(new SendMessage(task.getChatId(), task.getText()));
            service.deleteNotification(task);
        });
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            LOGGER.info("Processing update: {}", update);
            Message message = update.message();
            long chatId = message.chat().id();
            String messageText = service.responseText(message);
            telegramBot.execute(new SendMessage(chatId, messageText));
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
