package telegram.bot;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;


public class LilioraBot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            try {
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            File file=null;
            FindImage findImage = new FindImage();
                SendMessage message = new SendMessage()
                        .setChatId(chat_id)
                        .setText("Your search: "+message_text);


            try {
                file = findImage.find(message_text);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (file!=null) {
                SendPhoto photo = new SendPhoto()
                        .setChatId(chat_id)
                        .setPhoto(file);

                execute(message);
                execute(photo);
                findImage.removeTempFile();
            } else {
                message_text = "No result.Try again!";
                message = new SendMessage()
                        .setChatId(chat_id)
                        .setText("Your search: "+message_text);
                execute(message);
            }




            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public String getBotUsername() {
        // Return bot username
        // If bot username is @MyAmazingBot, it must return 'MyAmazingBot'
        return "LilioraBot";
    }

    @Override
    public String getBotToken() {
        // Return bot token from BotFather
        return "771383702:AAGXxBUczVeWvMjQEWcQtY79zu5tz4sUgDs";
    }
}