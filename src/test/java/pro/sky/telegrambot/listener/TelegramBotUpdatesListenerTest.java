package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.repository.NotificationRepository;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TelegramBotUpdatesListenerTest {
    @Mock
    private NotificationRepository repositoryMock;
    @Mock
    private TelegramBot telegramBotMock;

    @InjectMocks
    private TelegramBotUpdatesListener listener;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        listener.init();
    }

    @Test
    void scheduledSearch() {
//        Notification notification = new Notification()
//                .setId(1L)
//                .setText("Test notification")
//                .setDateTime(LocalDateTime.now())
//                .setChatId(12345L);
//
//        when(repositoryMock.findAllWithCurrentDateTime()).thenReturn(List.of(notification));
//
//        listener.scheduledSearch();

    }

    @Test
    void process() {
    }

    @Test
    void init() {
    }

    @Test
    void testScheduledSearch() {
    }

    @Test
    void testProcess() {
    }

    @Test
    void addNotificationTask() {
    }
}