package com.github.samen66.javarushtelegrambot.job;

import com.github.samen66.javarushtelegrambot.service.FindNewArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
@Component
public class FindNewArticleJob {
    private final FindNewArticleService findNewArticleService;
    @Autowired
    public FindNewArticleJob(FindNewArticleService findNewArticleService) {
        this.findNewArticleService = findNewArticleService;
    }

    @Scheduled(fixedRateString = "${bot.recountNewArticleFixedRate}")
    public void findNewPostJob(){
        LocalDateTime start = LocalDateTime.now();
        log.info("find new post job started");
        findNewArticleService.findNewArticles();
        LocalDateTime end = LocalDateTime.now();
        log.info("find new posts job stopped took seconds: {}",
                end.toEpochSecond(ZoneOffset.UTC) - start.toEpochSecond(ZoneOffset.UTC));

    }
}
