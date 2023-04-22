package com.github.samen66.javarushtelegrambot.service;

import com.github.samen66.javarushtelegrambot.entity.GroupSub;
import com.github.samen66.javarushtelegrambot.javarushclient.dto.GroupDiscussionInfo;
import com.github.samen66.javarushtelegrambot.repository.GroupSubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupSubServiceImpl implements GroupSubService{
    private final GroupSubRepository groupSubRepository;
    private final TelegramUserService telegramUserService;
    @Autowired
    public GroupSubServiceImpl(GroupSubRepository groupSubRepository, TelegramUserService telegramUserService) {
        this.groupSubRepository = groupSubRepository;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public GroupSub save(String chatId, GroupDiscussionInfo groupDiscussionInfo) {
        return null;
    }
}
