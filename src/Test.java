import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

public class Test extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Chat chat = update.getMessage().getChat();

            if (update.getMessage().hasText() || update.getMessage().getCaption() != null) {
                SendMessage textResponse;

                String text = update.getMessage().getText() == null
                        ? update.getMessage().getCaption()
                        : update.getMessage().getText();

                if (text.equals("chat_info")) {
                    String id, userName, firstName;

                    id = chat.getId().toString();
                    userName = chat.getUserName();
                    firstName = chat.getFirstName();

                    String response = String.format("Your chat id is: %s\n" +
                                                            "Your userName is %s\n" +
                                                            "Your first name is %s\n\n" +
                                                            "Detailed info: \n%s",
                                                    id, userName, firstName, chat.toString());

                    textResponse = new SendMessage()
                            .setChatId(update.getMessage().getChatId())
                            .setText(response);

                    textResponse.setReplyToMessageId(update.getMessage().getMessageId());
                } else if (text.toLowerCase().split(" ")[0].equals("photo_info")){
                    SendPhoto sendPhoto = new SendPhoto()
                            .setChatId(chat.getId())
                            .setPhoto(text.split(" ")[1])
                            .setCaption("Вот твоя фотка.")
                            .setReplyToMessageId(update.getMessage().getMessageId());
                    try {
                        sendPhoto(sendPhoto);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

                    textResponse = new SendMessage()
                            .setChatId(chat.getId())
                            .setText("your photo info is: " + sendPhoto);
                }                else {
                    textResponse = new SendMessage()
                            .setChatId(update.getMessage().getChatId())
                            .setText("caption is: " + update.getMessage().getCaption() +
                                             "\ntext is: "  + update.getMessage().getText() +
                                             "\n" + update.getMessage());
                }

                try {
                    execute(textResponse);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if (update.getMessage().hasPhoto()) {
                SendMessage photoResponse = new SendMessage()
                        .setChatId(chat.getId())
                        .setText("Your photo info is: " + update.getMessage().getPhoto().toString());
                try {
                    execute(photoResponse);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "telegramOS";
    }

    @Override
    public String getBotToken() {
        return "568533310:AAGmeJcc53P0VaG-GCSQlQhqO0jGWmklpPs";
    }

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        try {
            telegramBotsApi.registerBot(new Test());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
