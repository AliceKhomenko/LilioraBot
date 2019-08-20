package telegram.bot;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class LilioraBot extends TelegramLongPollingBot {



    @Override
    public void onUpdateReceived(Update update) {

        String message_text = null;
        FindImage findImage = new FindImage();
        Boolean hasResult = false;
        // String message_text = null;11

        if (("/start").equals(update.getMessage().getText()))
            startMessage(update);
        if ("/help".equals(update.getMessage().getText()))
            helpMessage(update);
        if (update.hasMessage() && update.getMessage().hasText()) {
            if (update.getMessage().getText().startsWith("/search"))
                hasResult = searchMessage(update);
            if (update.getMessage().getText().startsWith("/next")) {

                nextMessage(update);
            }
            }
        }

    public void helpMessage(Update update) {
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId())
                .setText("My name is LilioraBot. I can search images for you. " +
                        "For search first image try:\n /search your request, for example /search unicorn.\n" +
                        "For searching other image send: \n /next your request, for example: /next unicorn");
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public synchronized void setButtons(SendMessage sendMessage) {
        // Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardFirstRow.add(new KeyboardButton("/help"));

        // Вторая строчка клавиат

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        // и устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);
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


    public void startMessage(Update update) {
        try {        SendMessage message= new SendMessage().setChatId(update.getMessage().getChatId())
                .setText("Hello! I'm LilioraBot. I can search images for you. Please, read /help before using me.");
setButtons(message);
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public Boolean searchMessage(Update update){
        String message_text = null;
        FindImage findImage = new FindImage();
        Boolean hasResult = false;
        long chat_id = update.getMessage().getChatId();
        try {
            message_text = update.getMessage().getText().replaceAll("/search", "");
            File file = null;

            SendMessage message = new SendMessage()
                    .setChatId(chat_id)
                    .setText("Your search: " + message_text);
            // setButtons(message);


            try {
                file = findImage.find(message_text);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (file != null) {
                hasResult = true;
                SendPhoto photo = new SendPhoto()
                        .setChatId(chat_id)
                        .setPhoto(file);

                execute(message);
                execute(photo);
                findImage.removeTempFile();
            } else {
                hasResult = false;
                String default_text = "No result.Try again!";
                message = new SendMessage()
                        .setChatId(chat_id)
                        .setText(default_text);
                execute(message);
            }


        } catch (TelegramApiException e) {
            e.printStackTrace();
            return false;
        }
        return hasResult;

    }

    public void nextMessage(Update update){
        String message_text = null;
        FindImage findImage = new FindImage();
        Boolean hasResult = false;
        long chat_id = update.getMessage().getChatId();
        message_text = update.getMessage().getText().replaceAll("/next", "");

        File file = null;
        SendMessage message = new SendMessage()
                .setChatId(chat_id)
                .setText("Your search: " + message_text);


        try {
            file = findImage.findNext(message_text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (file != null) {
                hasResult = true;
                SendPhoto photo = new SendPhoto()
                        .setChatId(chat_id)
                        .setPhoto(file);

                execute(message);
                execute(photo);
                findImage.removeTempFile();
            } else {
                hasResult = false;
                String default_text = "No result.Try again!";
                message = new SendMessage()
                        .setChatId(chat_id)
                        .setText(default_text);
                execute(message);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();

        }

            findImage.removeTempFile();

        }

        public void getLastRequest(){




        }
    }



