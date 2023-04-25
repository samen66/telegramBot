package com.github.samen66.javarushtelegrambot.service;

import com.github.samen66.javarushtelegrambot.dto.GroupStatDTO;
import com.github.samen66.javarushtelegrambot.dto.StatisticDTO;
import com.github.samen66.javarushtelegrambot.entity.TelegramUser;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService{
    private final GroupSubService groupSubService;
    private final TelegramUserService telegramUserService;

    public StatisticsServiceImpl(GroupSubService groupSubService, TelegramUserService telegramUserService) {
        this.groupSubService = groupSubService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public StatisticDTO countBotStatistic() {
        List<GroupStatDTO> groupStatDTOList = groupSubService.findAll().stream()
                .filter(it -> !CollectionUtils.isEmpty(it.getUsers()))
                .map(groupSub -> new GroupStatDTO(groupSub.getId(), groupSub.getTitle(), groupSub.getUsers().size()))
                .collect(Collectors.toList());
        List<TelegramUser> activeUsers = telegramUserService.findAllActiveUsers();
        List<TelegramUser> inActiveUsers = telegramUserService.findAllInActiveUsers();
        Double avrG = averageGroupCountByUserColculate(activeUsers);
        StatisticDTO statisticDTO = new StatisticDTO(activeUsers.size(), inActiveUsers.size(), groupStatDTOList, avrG);
        return statisticDTO;
    }

    private Double averageGroupCountByUserColculate(List<TelegramUser> activeUsers) {
        return (double) activeUsers.stream().mapToInt(it -> it.getGroupSubs().size()).sum() / activeUsers.size();
    }

}
