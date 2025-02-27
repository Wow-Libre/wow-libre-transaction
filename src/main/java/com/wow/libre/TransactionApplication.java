package com.wow.libre;

import com.wow.libre.infrastructure.client.*;
import org.slf4j.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.*;
import org.springframework.scheduling.annotation.*;

@EnableScheduling
@SpringBootApplication
public class TransactionApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionApplication.class);

    public static void main(String[] args) {
        // Ejecutar la aplicación Spring Boot y obtener el contexto
        ApplicationContext context = SpringApplication.run(TransactionApplication.class, args);

        // Obtener el bean de Telegram del contexto
        Telegram telegramBot = context.getBean(Telegram.class);

        // Registrar el bot con TelegramBotsApi
       
    }


}
