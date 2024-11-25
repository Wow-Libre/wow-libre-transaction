package com.wow.libre.application.services.telegram;

import com.wow.libre.domain.port.in.*;
import org.springframework.stereotype.*;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.*;

import java.util.*;

@Component
public class TelegramService implements TelegramPort {
    private static SendMessage getSendMessage(Long chatId, String txt, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(txt);
        message.setReplyMarkup(inlineKeyboardMarkup);
        return message;
    }

    @Override
    public SendMessage sendRealmMenu(Long chatId) {
        // Crear botones para el menú de Realmlist
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("wowlibre");
        button1.setCallbackData("/realmlist-wowlibre");

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("Volver al menú principal");
        backButton.setCallbackData("/start");

        List<InlineKeyboardButton> row1 = Arrays.asList(button1, backButton);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(row1));

        return getSendMessage(chatId, "Encuentra los realmlist /realmlist-nombre", inlineKeyboardMarkup);
    }

    @Override
    public SendMessage sendMenuDefault(Long chatId, String text) {
        // Crear los botones
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Comando /start");
        button1.setCallbackData("/start");

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Comando /help");
        button2.setCallbackData("/help");

        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText("Comando /realmlist");
        button3.setCallbackData("/realmlist");

        // Crear filas de botones
        List<InlineKeyboardButton> row1 = Arrays.asList(button1, button2);
        List<InlineKeyboardButton> row2 = List.of(button3);

        // Configurar el teclado inline
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(Arrays.asList(row1, row2));

        return getSendMessage(chatId, text, inlineKeyboardMarkup);
    }

    @Override
    public String dynamicRealmlist(String name) {
        if (name.equalsIgnoreCase("wowlibre")) {
            return "set realmlist localhost";
        }
        return "No se encuentra";
    }
}
