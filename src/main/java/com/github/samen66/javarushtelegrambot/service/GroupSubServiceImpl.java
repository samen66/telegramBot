package com.github.samen66.javarushtelegrambot.service;

import com.github.samen66.javarushtelegrambot.entity.GroupSub;
import com.github.samen66.javarushtelegrambot.entity.TelegramUser;
import com.github.samen66.javarushtelegrambot.javarushclient.dto.GroupDiscussionInfo;
import com.github.samen66.javarushtelegrambot.repository.GroupSubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.Optional;

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
        TelegramUser telegramUser = telegramUserService.findByChatId(chatId).orElseThrow(NotFoundException::new);
        //TODO add Exception handler
        GroupSub groupSub;
        Optional<GroupSub> groupSubFromDatabase = groupSubRepository.findById(groupDiscussionInfo.getId());
        if (groupSubFromDatabase.isPresent()){
            groupSub = groupSubFromDatabase.get();
            Optional<TelegramUser> telegramUser1 = groupSub.getUsers().stream().filter(it -> it.getChatId().equalsIgnoreCase(chatId)).findFirst();
            if (telegramUser1.isEmpty()){
                groupSub.addUser(telegramUser);
            }
        }else{
            groupSub = new GroupSub();
            groupSub.setId(groupDiscussionInfo.getId());
            groupSub.addUser(telegramUser);
            groupSub.setTitle(groupDiscussionInfo.getTitle());
        }

        return groupSubRepository.save(groupSub);
    }
}
