package com.github.samen66.javarushtelegrambot.repository;

import com.github.samen66.javarushtelegrambot.entity.TelegramUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TelegramUserRepositoryIT {
    @Autowired
    private TelegramUserRepository telegramUserRepository;
    @Sql(scripts = {"/sql/clearDbs.sql", "/sql/telegram_users.sql"})
    @Test
    public void shouldProperlyFindAllActiveUsers(){
        List<TelegramUser> activeUsers = telegramUserRepository.findAllByActiveTrue();
        Assertions.assertEquals(5, activeUsers.size());
    }
    @Sql(scripts = {"/sql/clearDbs.sql"})
    @Test
    public void shouldProperlySaveTelegramUser(){
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setChatId("1234567890");
        telegramUser.setActive(false);
        telegramUserRepository.save(telegramUser);

        Optional<TelegramUser> optionalTelegramUser = telegramUserRepository.findById(telegramUser.getChatId());
        Assertions.assertTrue(optionalTelegramUser.isPresent());
        Assertions.assertEquals(telegramUser, optionalTelegramUser.get());
    }
}