package com.example.pract.service;

import com.example.pract.controller.MainController;
import com.example.pract.entity.News;
import com.example.pract.enums.NewsType;
import com.example.pract.repository.CriminalRepository;
import com.example.pract.repository.NewsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsService {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    private final NewsRepository newsRepository;

    @Autowired
    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public News addNews(String title, String description, NewsType type) {
        News news = new News();
        news.setTitle(title);
        news.setDescription(description);
        news.setType(type);

        return newsRepository.save(news);
    }
}
