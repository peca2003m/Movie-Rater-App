package com.example.movie_rater.service;

import com.example.movie_rater.dto.UserReview;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StompService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotification(String imdbId, UserReview review) {
        messagingTemplate.convertAndSend("/topic/" + imdbId, review);
    }


}
