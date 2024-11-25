package com.wow.libre;

import com.wow.libre.infrastructure.client.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.*;
import org.springframework.scheduling.annotation.*;
import org.telegram.telegrambots.meta.*;
import org.telegram.telegrambots.meta.exceptions.*;
import org.telegram.telegrambots.updatesreceivers.*;

@EnableScheduling
@SpringBootApplication
public class TransactionApplication {

    public static void main(String[] args) {
        // Ejecutar la aplicación Spring Boot y obtener el contexto
        ApplicationContext context = SpringApplication.run(TransactionApplication.class, args);

        // Obtener el bean de Telegram del contexto
        Telegram telegramBot = context.getBean(Telegram.class);

        // Registrar el bot con TelegramBotsApi
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
