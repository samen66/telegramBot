package com.github.samen66.javarushtelegrambot.service;

import com.github.samen66.javarushtelegrambot.entity.GroupSub;
import com.github.samen66.javarushtelegrambot.entity.PostInfo;
import com.github.samen66.javarushtelegrambot.entity.TelegramUser;
import com.github.samen66.javarushtelegrambot.javarushclient.JavaRushPostClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FindNewArticleServiceIml implements FindNewArticleService{
    private static final String JAVARUSH_WEB_POST_LINK = "https://javarush.ru/api/1.0/rest/posts/%S";

    private final SendBotMessageService sendBotMessageService;
    private final GroupSubService groupSubService;
    private final JavaRushPostClient javaRushPostClient;

    @Autowired
    public FindNewArticleServiceIml(SendBotMessageService sendBotMessageService, GroupSubService groupSubService, JavaRushPostClient javaRushPostClient) {
        this.sendBotMessageService = sendBotMessageService;
        this.groupSubService = groupSubService;
        this.javaRushPostClient = javaRushPostClient;
    }

    @Override
    public void findNewArticles() {
        groupSubService.findAll().forEach(
                groupSub -> {
                    Integer lastPostId = groupSub.getLastArticleId();
                    if (lastPostId == null){
                        lastPostId = 0;
                    }
                    List<PostInfo> newPostsList = javaRushPostClient.findNewPosts(groupSub.getId(), lastPostId);

                    setLastPostId(groupSub, newPostsList);
                    sendNewPostsToUsers(groupSub, newPostsList);
                }

        );

    }

    private void sendNewPostsToUsers(GroupSub groupSub, List<PostInfo> newPostsList) {
        Collections.reverse(newPostsList);
        List<String> newPostMessages = newPostsList.stream().map(post ->
            String.format("✨Вышла новая статья <b>%s</b> в группе <b>%s</b>.✨\n\n" +
                            "<b>Описание:</b> %s\n\n" +
                            "<b>Ссылка:</b> %s\n",
                    post.getTitle(), groupSub.getTitle(), post.getDescription(), getPostUrl(post.getKey())))
                .collect(Collectors.toList());

        groupSub.getUsers().stream().filter(TelegramUser::isActive)
                .forEach(it -> sendBotMessageService.sendMessage(it.getChatId(), newPostMessages));
    }

    private String getPostUrl(String key) {
        return String.format(JAVARUSH_WEB_POST_LINK, key);
    }

    private void setLastPostId(GroupSub groupSub, List<PostInfo> newPostsList) {
        newPostsList.stream().mapToInt(PostInfo::getId).max()
                .ifPresent(it ->{
                    groupSub.setLastArticleId(it);
                    groupSubService.save(groupSub);
                });
    }
}
