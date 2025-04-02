package com.example.flexiMed.ambulances;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class AmbulanceLocationHandler extends TextWebSocketHandler {
    private final ConcurrentHashMap<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String userId = getUserIdFromSession(session);
        userSessions.put(userId, session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        // No need to handle incoming messages
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userId = getUserIdFromSession(session);
        userSessions.remove(userId);
    }

    public void sendLocationToUser(String userId, String locationJson) {
        WebSocketSession session = userSessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(locationJson));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getUserIdFromSession(WebSocketSession session) {
        // Extract user ID from query params (e.g., ws://localhost:8080/ws/ambulance-updates?userId=USER123)
        String query = session.getUri().getQuery();
        return query != null && query.startsWith("userId=") ? query.split("=")[1] : "unknown";
    }
}

