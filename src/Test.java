import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public class Test extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        //TODO implement later
    }

    @Override
    public String getBotUsername() {
        return "telegramOS";
    }

    @Override
    public String getBotToken() {
        return "568533310:AAGmeJcc53P0VaG-GCSQlQhqO0jGWmklpPs";
    }
}
