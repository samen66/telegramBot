package com.github.samen66.javarushtelegrambot.command;

import com.github.samen66.javarushtelegrambot.dto.StatisticDTO;
import com.github.samen66.javarushtelegrambot.service.SendBotMessageService;
import com.github.samen66.javarushtelegrambot.service.StatisticsService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.stream.Collectors;

public class StatCommand implements Command{
    private final StatisticsService statisticsService;
    private final SendBotMessageService sendBotMessageService;

    public final static String STAT_MESSAGE = "✨<b>Подготовил статистику</b>✨\\n\" +\n" +
            "           \"- Количество активных пользователей: %s\\n\" +\n" +
            "           \"- Количество неактивных пользователей: %s\\n\" +\n" +
            "           \"- Среднее количество групп на одного пользователя: %s\\n\\n\" +\n" +
            "           \"<b>Информация по активным группам</b>:\\n\" +\n" +
            "           \"%s";

    public StatCommand(StatisticsService statisticsService, SendBotMessageService sendBotMessageService) {
        this.statisticsService = statisticsService;
        this.sendBotMessageService = sendBotMessageService;
    }


    @Override
    public void execute(Update update) {
        StatisticDTO statisticDTO = statisticsService.countBotStatistic();

        String collectedGroups = statisticDTO.getGroupStatDTOs().stream()
                .map(it -> String.format("%s (id = %s) - %s подписчиков", it.getTitle(), it.getId(), it.getActiveUserCount()))
                .collect(Collectors.joining("\n"));

        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), String.format(STAT_MESSAGE,
                statisticDTO.getActiveUserCount(),
                statisticDTO.getInactiveUserCount(),
                statisticDTO.getAverageGroupCountByUser(),
                collectedGroups));
    }
}
