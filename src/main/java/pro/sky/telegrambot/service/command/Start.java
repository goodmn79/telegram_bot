package pro.sky.telegrambot.service.command;

import com.pengrad.telegrambot.model.Message;
import org.springframework.stereotype.Component;

import static pro.sky.telegrambot.service.service_message.NotificationMessage.*;

@Component
public class Start implements Command {
    @Override
    public String createReplyMessage(Message message) {
        return messageBuilder(WELCOME, MENU);
    }
}
