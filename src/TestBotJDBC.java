import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.sql.*;
import java.util.Properties;

public class TestBotJDBC extends TelegramLongPollingBot {
    private static Connection connection;

    @Override
    public void onUpdateReceived(Update update) {
        String input = update.getMessage().getText();
        SendMessage answer = new SendMessage()
                .setChatId(update.getMessage().getChatId());


        try (PreparedStatement statement = connection.prepareStatement(input.split(";")[1])) {
            ResultSet resultSet = statement.executeQuery();

            StringBuilder builder = new StringBuilder("");
            for (String column : input.split(";")[0].split(" "))
                builder.append(column.split(":")[0]).append(" ");
            builder.append("\n");

            while (resultSet.next()) {
                for (String column : input.split(";")[0].split(" ")) {
                    if (column.split(":")[1].toLowerCase().equals("string")) {
                        builder.append(resultSet.getString(column.split(":")[0])).append(" ");
                    } else {
                        builder.append(resultSet.getInt(column.split(":")[0])).append(" ");
                    }
                }
                builder.append("\n");
            }

            answer = answer.setText(builder.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            answer = answer.setText("Bad query");
        }

        try {
            execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private void log(String message, long chatID) {
        SendMessage logMessage = new SendMessage()
                .setChatId(chatID)
                .setChatId(message);
        try {
            execute(logMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
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


    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Properties properties = new Properties();
        properties.setProperty("user", "postgres");
        properties.setProperty("password", "dazachemmnevoobsheparol");
        connection = DriverManager.getConnection("jdbc:postgresql:ifmo",
                                                 properties);

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new TestBotJDBC());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
