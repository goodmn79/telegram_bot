package pro.sky.telegrambot.service.command;

import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.service.NotificationTaskService;

@Component
@RequiredArgsConstructor
public class Notification implements Command {
    private final NotificationTaskService service;

    @Override
    public String createReplyMessage(Message message) {
        long chatId = message.chat().id();
        return service.getAllByChatId(chatId);
    }
}
