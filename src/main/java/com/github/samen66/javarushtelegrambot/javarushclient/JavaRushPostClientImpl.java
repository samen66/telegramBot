package com.github.samen66.javarushtelegrambot.javarushclient;

import com.github.samen66.javarushtelegrambot.entity.PostInfo;
import kong.unirest.GenericType;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class JavaRushPostClientImpl implements JavaRushPostClient{

    private final String javaRushPostAPIPath;

    public JavaRushPostClientImpl(@Value("${javarush.api.path}") String javaRushPostAPIPath) {
        this.javaRushPostAPIPath = javaRushPostAPIPath + "/posts";
    }

    @Override
    public List<PostInfo> findNewPosts(Integer groupId, Integer lastPostId) {
        List<PostInfo> postInfoList = Unirest.get(javaRushPostAPIPath)
                .queryString("order", "NEW")
                .queryString("groupKid", groupId)
                .queryString("limit", 15)
                .asObject(new GenericType<List<PostInfo>>() {
                }).getBody();
        List<PostInfo> newPostsList = new ArrayList<>();
        for(PostInfo postInfo : postInfoList){
            if (lastPostId.equals(postInfo.getId())){
                return newPostsList;
            }
            newPostsList.add(postInfo);
        }
        return newPostsList;
    }
}
