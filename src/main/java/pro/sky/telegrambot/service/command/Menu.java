package pro.sky.telegrambot.service.command;

import com.pengrad.telegrambot.model.Message;
import org.springframework.stereotype.Component;

import static pro.sky.telegrambot.service.service_message.NotificationMessage.*;

@Component
public class Menu implements Command {
    @Override
    public String createReplyMessage(Message message) {
        return messageBuilder(MENU_ELEMENT_1, MENU_ELEMENT_2);
    }
}
