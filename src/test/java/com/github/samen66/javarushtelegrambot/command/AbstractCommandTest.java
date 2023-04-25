package com.github.samen66.javarushtelegrambot.command;

import com.github.samen66.javarushtelegrambot.bot.JavaRushTelegramBot;
import com.github.samen66.javarushtelegrambot.entity.GroupSub;
import com.github.samen66.javarushtelegrambot.service.SendBotMessageService;
import com.github.samen66.javarushtelegrambot.service.SendBotMessageServiceImpl;
import com.github.samen66.javarushtelegrambot.service.StatisticsService;
import com.github.samen66.javarushtelegrambot.service.TelegramUserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.github.samen66.javarushtelegrambot.command.CommandName.LIST_GROUP_SUB;

/**
 * Abstract class for testing {@link Command}s.
 */
public abstract class AbstractCommandTest {
    protected JavaRushTelegramBot javarushBot = Mockito.mock(JavaRushTelegramBot.class);
    protected SendBotMessageService sendBotMessageService = new SendBotMessageServiceImpl(javarushBot);
    protected TelegramUserService telegramUserService = Mockito.mock(TelegramUserService.class);
    protected StatisticsService statisticsService = Mockito.mock(StatisticsService.class);
    abstract String getCommandName();

    abstract String getCommandMessage();

    abstract Command getCommand();

    @Test
    public void shouldProperlyExecuteCommand() throws TelegramApiException {
        //given
        Long chatId = 1234567824356L;

        Update update = new Update();
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(message.getText()).thenReturn(getCommandName());
        update.setMessage(message);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(getCommandMessage());
        sendMessage.enableHtml(true);

        //when
        getCommand().execute(update);

        //then
        Mockito.verify(javarushBot).execute(sendMessage);
    }

    public static Update prepareUpdate(Long chatId, String text){
        Message message = Mockito.mock(Message.class);
        Update update = new Update();
        Mockito.when(message.getChatId()).thenReturn(Long.parseLong(chatId.toString()));
        Mockito.when(message.getText()).thenReturn(text);
        Mockito.when(message.hasText()).thenReturn(true);
        update.setMessage(message);
        return update;
    }
    public static GroupSub creatGroupSub(int groupId, String groupTitle){
        GroupSub groupSub = new GroupSub();
        groupSub.setTitle(groupTitle);
        groupSub.setId(groupId);
        return groupSub;
    }
}
