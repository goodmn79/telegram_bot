package pro.sky.telegrambot.service.service_message;

public final class NotificationMessage {
    public static final String START = "/start";
    public static final String MENU = "/menu";
    public static final String NOTIFICATIONS = "/notifications";
    public static final String INFO = "/info";
    public static final String WELCOME = "Привет! Я Telegram бот. Я присылаю уведомления, в установленное время.";
    public static final String MENU_ELEMENT_1 = messageBuilder(INFO, " инструкция");
    public static final String MENU_ELEMENT_2 = messageBuilder(NOTIFICATIONS, " посмотреть все уведомления");
    public static final String INFO_TEXT = "Для создания уведомления укажите дату и время, затем напишите текст уведомления. Дата и время должны быть указаны в формате 'ДД.ММ.ГГГГ ЧЧ:ММ', иметь валидные значения и не могут быть раньше или равны текущему времени.Для добавления уведомления на текущий день дату можно не указывать.\n/menu";
    public static final String SUCCESSFULLY_CREATED = "Уведомление успешно создано!\n/menu";
    public static final String NO_NOTIFICATIONS = "Вы ещё не создали ни одного уведомления!\n/menu";

    private NotificationMessage() {
    }

    public static String messageBuilder(String... strings) {
        StringBuilder stringBuilder = new StringBuilder(strings[0]);
        for (int i = 1; i < strings.length; i++) {
            String whiteSpace = "\n" + strings[i];
            stringBuilder.append(whiteSpace);
        }
        return stringBuilder.toString();
    }
}
