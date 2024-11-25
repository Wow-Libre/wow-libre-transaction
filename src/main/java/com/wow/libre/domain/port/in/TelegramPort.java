package com.wow.libre.domain.port.in;

import org.telegram.telegrambots.meta.api.methods.send.*;

public interface TelegramPort {
    SendMessage sendRealmMenu(Long chatId);

    SendMessage sendMenuDefault(Long chatId, String text);

    String dynamicRealmlist(String nombre);
}
