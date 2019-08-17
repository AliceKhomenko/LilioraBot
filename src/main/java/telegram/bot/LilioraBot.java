package telegram.bot;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;


public class LilioraBot extends TelegramLongPollingBot {
    String message_text = null;
    FindImage findImage = new FindImage();
    Boolean hasResult = false;

    int i = 1;

    @Override
    public void onUpdateReceived(Update update) {
        long chat_id = update.getMessage().getChatId();


        // String message_text = null;

        if (("/start").equals(update.getMessage().getText()))
            startMessage(update);
        if (update.hasMessage() && update.getMessage().hasText()) {
            i = 1;
            if (update.getMessage().getText().startsWith("/search"))
                hasResult = searchMessage(update);
            if (update.getMessage().getText().equals("/next") && message_text != null) {
                if (hasResult == true) {
                    nextMessage(update);
                } else {
                    hasResult = false;
                    String default_text = "No result.Try again!";
                    SendMessage message = new SendMessage()
                            .setChatId(chat_id)
                            .setText(default_text);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }


                }
            }
        }
    }

 /*  public synchronized void setButtons(SendMessage sendMessage) {
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
        keyboardFirstRow.add(new KeyboardButton("/next"));

        // Вторая строчка клавиат

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        // и устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);
    }*/

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
        try {
            execute(new SendMessage().setChatId(update.getMessage().getChatId())
                    .setText("Hello! I'm LilioraBot. I can search images for you. Please, read /help before using me."));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public Boolean searchMessage(Update update){
        Boolean hasResult=false;
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
        long chat_id = update.getMessage().getChatId();

        File file = null;
        SendMessage message = new SendMessage()
                .setChatId(chat_id)
                .setText("Your search: " + message_text);


        try {
            file = findImage.findNext(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file != null) {
            SendPhoto photo = new SendPhoto()
                    .setChatId(chat_id)
                    .setPhoto(file);

            try {
                execute(message);
                execute(photo);
            } catch (TelegramApiException e) {
                e.printStackTrace();
                try {
                    execute(new SendMessage()
                            .setChatId(chat_id)
                            .setText("Sorry, something went wrong"));
                } catch (TelegramApiException e1) {
                    e1.printStackTrace();
                }
            }

            findImage.removeTempFile();
            i++;
        }
    }


    }
