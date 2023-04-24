package com.github.samen66.javarushtelegrambot.javarushclient;

import com.github.samen66.javarushtelegrambot.entity.PostInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Integration-level testing for JavaRushPostClient")
class JavaRushPostClientImplTest {
    private static final String JAVARUSH_API_PATH = "https://javarush.ru/api/1.0/rest";
    private JavaRushPostClient javaRushPostClient = new JavaRushPostClientImpl(JAVARUSH_API_PATH);
    @Test
    public void shouldProperlyGet15Posts(){
        List<PostInfo> postInfoList = javaRushPostClient.findNewPosts(30, 2935);

        Assertions.assertEquals(15, postInfoList.size());
    }


}