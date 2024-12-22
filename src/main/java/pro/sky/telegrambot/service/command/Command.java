package pro.sky.telegrambot.service.command;

import com.pengrad.telegrambot.model.Message;

public interface Command {
    String createReplyMessage(Message message);
}
