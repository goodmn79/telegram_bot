package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationRepository;
import pro.sky.telegrambot.service.extractor.DataExtractor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.EMPTY_LIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static pro.sky.telegrambot.service.service_message.NotificationMessage.*;

@ExtendWith(MockitoExtension.class)
class NotificationTaskServiceTest {
    @Mock
    private NotificationRepository repository;
    @Mock
    private DataExtractor extractor;

    @InjectMocks
    private NotificationTaskService service;

    private Message message;
    private Chat chat;
    private LocalDateTime currentDateTime;
    private String text;
    private long chatId;
    private NotificationTask notificationTask;
    List<NotificationTask> notifications;

    @BeforeEach
    void init() {
        service.telegramBotCommandsInit();
        message = mock(Message.class);
        chat = mock(Chat.class);
        currentDateTime = LocalDateTime.now();
        chatId = 12345L;
        text = "notification text";
        notificationTask = new NotificationTask()
                .setLocalDateTime(currentDateTime)
                .setText(text)
                .setChatId(chatId);
        notifications = new ArrayList<>(List.of(notificationTask));
    }

    @Test
    void responseText_shouldReturnMenuMessage() {
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(message.text()).thenReturn(MENU);

        String actual = service.responseText(message);

        assertThat(actual).isEqualTo(messageBuilder(MENU_ELEMENT_1, MENU_ELEMENT_2));
    }

    @Test
    void responseText_shouldReturnStartMessage() {
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(message.text()).thenReturn(START);

        String actual = service.responseText(message);

        assertThat(actual).isEqualTo(messageBuilder(WELCOME, MENU));
    }

    @Test
    void responseText_shouldReturnInfoMessage() {
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(message.text()).thenReturn(INFO);

        String actual = service.responseText(message);

        assertThat(actual).isEqualTo(INFO_TEXT);
    }

    @Test
    void responseText_shouldReturnInfoMessage_shouldReturnStringRepresentationAllNotificationsWithThisChatId() {
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(message.text()).thenReturn(NOTIFICATIONS);
        when(repository.findAllByChatId(chatId)).thenReturn(notifications);

        String actual = service.responseText(message);

        verify(repository).findAllByChatId(chatId);
        assertThat(actual).contains(
                notificationTask.stringRepresentationDateTime(),
                notificationTask.getText());
    }

    @Test
    void responseText_whenNoNotification_shouldReturnDesiredMessage() {
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(message.text()).thenReturn(NOTIFICATIONS);
        when(repository.findAllByChatId(12345L)).thenReturn(EMPTY_LIST);

        String actual = service.responseText(message);

        verify(repository).findAllByChatId(chatId);
        assertThat(actual).isEqualTo(NO_NOTIFICATIONS);
    }

    @Test
    void responseText_whenCorrectNotificationTask_shouldReturnDesiredMessage() {
        notificationTask.setLocalDateTime(currentDateTime.plusDays(1));
        String dateTimeString = notificationTask.stringRepresentationDateTime();
        String messageText = String.format("%s %s", dateTimeString, text);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(message.text()).thenReturn(messageText);
        when(extractor.extractDateTime(messageText)).thenReturn(notificationTask.getLocalDateTime());
        when(extractor.extractText(messageText)).thenReturn(notificationTask.getText());
        when(repository.save(notificationTask)).thenReturn(notificationTask);

        String actual = service.responseText(message);

        verify(repository).save(notificationTask);
        verify(extractor).extractDateTime(messageText);
        verify(extractor).extractText(messageText);
        assertThat(actual).isEqualTo(SUCCESSFULLY_CREATED);
    }

    @Test
    void responseText_whenInvalidDateTime_shouldReturnDesiredMessage() {
        String dateTimeString = notificationTask.stringRepresentationDateTime();
        String messageText = String.format("%s %s", dateTimeString, text);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(message.text()).thenReturn(messageText);
        when(extractor.extractDateTime(messageText)).thenReturn(notificationTask.getLocalDateTime());

        String actual = service.responseText(message);

        verify(extractor).extractDateTime(messageText);
        assertThat(actual).isEqualTo(INFO_TEXT);
    }

    @Test
    void getCurrentTimeTasks_ShouldReturnNotificationsWithCurrentTime() {
        when(repository.findAllWithCurrentDateTime()).thenReturn(notifications);

        Collection<NotificationTask> actual = service.getCurrentTimeTasks();

        verify(repository).findAllWithCurrentDateTime();
        assertThat(actual).isNotNull().hasSize(1);
        assertThat(actual).allMatch(n -> n.getLocalDateTime().isEqual(currentDateTime));
    }

    @Test
    void deleteNotification_shouldCallRepositoryDelete() {
        service.deleteNotification(notificationTask);

        verify(repository).delete(notificationTask);
    }
}