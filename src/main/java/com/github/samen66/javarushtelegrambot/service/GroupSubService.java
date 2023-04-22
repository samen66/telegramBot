package com.github.samen66.javarushtelegrambot.service;

import com.github.samen66.javarushtelegrambot.entity.GroupSub;
import com.github.samen66.javarushtelegrambot.javarushclient.dto.GroupDiscussionInfo;

/**
 * Service for manipulating with {@link GroupSub}.
 */
public interface GroupSubService {

    GroupSub save(String chatId, GroupDiscussionInfo groupDiscussionInfo);
}
