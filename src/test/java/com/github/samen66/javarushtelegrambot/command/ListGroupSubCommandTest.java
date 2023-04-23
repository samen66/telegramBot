package com.github.samen66.javarushtelegrambot.command;

import com.github.samen66.javarushtelegrambot.entity.GroupSub;
import com.github.samen66.javarushtelegrambot.entity.TelegramUser;
import com.github.samen66.javarushtelegrambot.service.SendBotMessageService;
import com.github.samen66.javarushtelegrambot.service.TelegramUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.samen66.javarushtelegrambot.command.CommandName.LIST_GROUP_SUB;
import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Unit-level testing for ListGroupSubCommandTest")
class ListGroupSubCommandTest {
    @Test
    public void shouldProperlySendListOfGroupSub() {
        //given
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setChatId("1");
        telegramUser.setActive(true);

        List<GroupSub> groupSubList = new ArrayList<>();
        groupSubList.add(creatGroupSub(2, "Almaty"));
        groupSubList.add(creatGroupSub(1, "JavaRush"));
        groupSubList.add(creatGroupSub(3, "Moscow"));
        telegramUser.setGroupSubs(groupSubList);
        SendBotMessageService sendBotMessageService = Mockito.mock(SendBotMessageService.class);
        TelegramUserService telegramUserService = Mockito.mock(TelegramUserService.class);

        Mockito.when(telegramUserService.findByChatId(telegramUser.getChatId())).thenReturn(Optional.of(telegramUser));
        ListGroupSubCommand groupSubCommand = new ListGroupSubCommand(telegramUserService, sendBotMessageService);

        Message message = Mockito.mock(Message.class);
        Update update = new Update();
        Mockito.when(message.getChatId()).thenReturn(Long.parseLong(telegramUser.getChatId()));
        Mockito.when(message.getText()).thenReturn(LIST_GROUP_SUB.getCommandName());
        update.setMessage(message);

        String messageText = "List of group that you followed \n\n";
        String messageListUserGroupSub = telegramUser.getGroupSubs().stream()
                .map(groupSub -> String.format("%s - %s\n", groupSub.getId(), groupSub.getTitle()))
                .collect(Collectors.joining());

        //when
        groupSubCommand.execute(update);

        //then
        Mockito.verify(sendBotMessageService).sendMessage(telegramUser.getChatId(), messageText + messageListUserGroupSub);

    }

    private GroupSub creatGroupSub(int groupId, String groupTitle){
        GroupSub groupSub = new GroupSub();
        groupSub.setTitle(groupTitle);
        groupSub.setId(groupId);
        return groupSub;
    }
}