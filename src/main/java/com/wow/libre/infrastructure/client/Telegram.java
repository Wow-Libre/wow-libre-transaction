package com.wow.libre.infrastructure.client;

import com.wow.libre.domain.port.in.*;
import org.springframework.stereotype.*;
import org.telegram.telegrambots.bots.*;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.*;

@Component
public class Telegram extends TelegramLongPollingBot {
    private final TelegramPort telegramPort;

    public Telegram(TelegramPort telegramPort) {
        super("7204107406:AAGnNBdKjuaw5Z7SZC7qw2Pb3pwlzWg9xUU");
        this.telegramPort = telegramPort;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Manejar comandos enviados como texto
            handleTextCommand(update.getMessage().getChatId(), update.getMessage().getText());
        } else if (update.hasCallbackQuery()) {
            // Manejar comandos desde botones inline
            handleCallbackCommand(update.getCallbackQuery().getMessage().getChatId(),
                    update.getCallbackQuery().getData());
        }
    }

    private void handleTextCommand(Long chatId, String command) {
        if (command.startsWith("/realmlist-")) {
            String nombre = command.substring("/realmlist-".length());
            sendTextMessage(chatId, telegramPort.dynamicRealmlist(nombre));
        } else {
            SendMessage sendMessage;
            switch (command) {
                case "/start":
                    sendMessage = telegramPort.sendMenuDefault(chatId, "¡Bienvenido! Seleccione una opción del menú:");
                    executeMessage(sendMessage);
                    break;
                case "/realmlist":
                    sendMessage = telegramPort.sendRealmMenu(chatId);
                    executeMessage(sendMessage);
                    break;
                default:

                    break;
            }
        }
    }

    private void handleCallbackCommand(Long chatId, String callbackData) {
        if (callbackData.startsWith("/realmlist-")) {
            String nombre = callbackData.substring("/realmlist-".length());
            sendTextMessage(chatId, telegramPort.dynamicRealmlist(nombre));
        } else {
            switch (callbackData) {
                case "/start":
                    SendMessage sendMessage = telegramPort.sendMenuDefault(chatId, "¡Has presionado el botón /start!");
                    executeMessage(sendMessage);
                    break;
                case "/realmlist":
                    SendMessage menuMessage = telegramPort.sendRealmMenu(chatId);
                    executeMessage(menuMessage);
                    break;
                default:
                    
                    break;
            }
        }
    }


    private void sendTextMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "WowLibre_bot";
    }

}
