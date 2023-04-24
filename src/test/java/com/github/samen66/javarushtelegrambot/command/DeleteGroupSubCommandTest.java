package com.github.samen66.javarushtelegrambot.command;

import com.github.samen66.javarushtelegrambot.entity.GroupSub;
import com.github.samen66.javarushtelegrambot.entity.TelegramUser;
import com.github.samen66.javarushtelegrambot.service.GroupSubService;
import com.github.samen66.javarushtelegrambot.service.SendBotMessageService;
import com.github.samen66.javarushtelegrambot.service.TelegramUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.github.samen66.javarushtelegrambot.command.AbstractCommandTest.creatGroupSub;
import static com.github.samen66.javarushtelegrambot.command.AbstractCommandTest.prepareUpdate;
import static com.github.samen66.javarushtelegrambot.command.CommandName.DELETE_GROUP_SUB;
import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Unit-level testing for DeleteGroupSubCommand")
class DeleteGroupSubCommandTest {
    private Command command;
    private SendBotMessageService sendBotMessageService;
    TelegramUserService telegramUserService;
    GroupSubService groupSubService;

    @BeforeEach
    public void init(){
        telegramUserService = Mockito.mock(TelegramUserService.class);
        groupSubService = Mockito.mock(GroupSubService.class);
        sendBotMessageService = Mockito.mock(SendBotMessageService.class);
        command = new DeleteGroupSubCommand(sendBotMessageService, groupSubService, telegramUserService);
    }

    @Test
    public void shouldSendNonListOfGroups(){
        //    когда просто передали команду /deleteGroupSub и нет подписок на группы;
        //given
        Long chatId = 23456L;
        String text = DELETE_GROUP_SUB.getCommandName();

        Update update = AbstractCommandTest.prepareUpdate(chatId, text);
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setChatId(String.valueOf(chatId));
        Mockito.when(telegramUserService.findByChatId(String.valueOf(chatId))).thenReturn(Optional.of(telegramUser));
        String expectedMessage = "Пока нет подписок на группы. Чтобы добавить подписку напиши /addGroupSub";
        //when
        command.execute(update);

        //then
        Mockito.verify(sendBotMessageService).sendMessage(String.valueOf(chatId), expectedMessage);
    }

    @Test
    public void shouldSendListOfUsersFollowedGroups(){
        //    когда просто передали команду /deleteGroupSub и есть подписки на группы;
        Long chatId = 23456L;
        String text = DELETE_GROUP_SUB.getCommandName();

        Update update = AbstractCommandTest.prepareUpdate(chatId, text);
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setChatId(String.valueOf(chatId));
        List<GroupSub> groupSubList = new ArrayList<>();
        groupSubList.add(creatGroupSub(123, "GS1 Title"));
        telegramUser.setGroupSubs(groupSubList);
        Mockito.when(telegramUserService.findByChatId(String.valueOf(chatId))).thenReturn(Optional.of(telegramUser));
        String expectedMessage = "Чтобы удалить подписку на группу - передай комадну вместе с ID группы. \n" +
                "Например: /deleteGroupSub 16 \n\n" +
                "я подготовил список всех групп, на которые ты подписан) \n\n" +
                "имя группы - ID группы \n\n" +
                "GS1 Title - 123\n";
        //when
        command.execute(update);

        //then
        Mockito.verify(sendBotMessageService).sendMessage(String.valueOf(chatId), expectedMessage);
        
    }
    @Test
    public void shouldSendErrorGroupIdMessage(){
        Long chatId = 23456L;
        String text = DELETE_GROUP_SUB.getCommandName() + " abs";
        Update update = prepareUpdate(chatId, text);
        Mockito.when(telegramUserService.findByChatId(chatId.toString())).thenReturn(Optional.of(new TelegramUser()));
        String exceptedMessage ="неправильный формат ID группы.\n " +
                "ID должно быть целым положительным числом";
        //when
        command.execute(update);

        //then
        Mockito.verify(sendBotMessageService).sendMessage(chatId.toString(), exceptedMessage);
    }


    //    сценарий, при котором все правильно удалится, как и ожидается;
    @Test
    public void shouldDeleteAsCorrecrt(){
        Long chatId = 23456L;
        String text = DELETE_GROUP_SUB.getCommandName() + " 3";
        Update update = prepareUpdate(chatId, text);
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setChatId(chatId.toString());
        List<GroupSub> groupSubList = new ArrayList<>();
        groupSubList.add(creatGroupSub(1, "JavaRush"));
        groupSubList.add(creatGroupSub(2, "Almaty"));
        GroupSub groupSub = new GroupSub();
        groupSub.setId(3);
        groupSub.setTitle("Moscow");
        groupSub.addUser(telegramUser);
        groupSubList.add(groupSub);
        telegramUser.setGroupSubs(groupSubList);
        Mockito.when(telegramUserService.findByChatId(chatId.toString())).thenReturn(Optional.of(telegramUser));
        Mockito.when(groupSubService.findById(3)).thenReturn(Optional.ofNullable(groupSubList.get(2)));
//        Mockito.when(groupSub.getUsers().remove(telegramUser)).thenReturn(true);
        String exceptedMessage = "Удалил подписку на группу: Moscow";
        //when
        command.execute(update);

        //then
//        users.remove(telegramUser);
        Mockito.verify(groupSubService).save(groupSub);
        Mockito.verify(sendBotMessageService).sendMessage(chatId.toString(), exceptedMessage);
    }
    //    сценарий, когда ID группы валидный, но такой группы нет в БД.
    @Test
    public void shouldSendGroupIdNotFound(){
        Long chatId = 23456L;
        Integer groupId = 123;
        String text = DELETE_GROUP_SUB.getCommandName() + " " + String.valueOf(groupId);
        Update update = prepareUpdate(chatId, text);
        Mockito.when(groupSubService.findById(groupId)).thenReturn(Optional.empty());
        String exceptedMessage = "Не нашел такой группы =/";

        //when
        command.execute(update);

        //then
        Mockito.verify(groupSubService).findById(groupId);
        Mockito.verify(sendBotMessageService).sendMessage(chatId.toString(), exceptedMessage);
    }

}