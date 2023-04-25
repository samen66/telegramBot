package com.github.samen66.javarushtelegrambot.service;

import com.github.samen66.javarushtelegrambot.dto.StatisticDTO;

/**
 * Service for getting bot statistics.
 */
public interface StatisticsService {
    StatisticDTO countBotStatistic();
}