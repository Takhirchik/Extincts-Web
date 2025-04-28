//package com.warlock.controller;
//
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.stereotype.Controller;
//
//import java.security.Principal;
//
//@Controller
//public class NotificationController {
//
//    @MessageMapping("/image-upload")
//    public void handleImageUpload(@Payload Image Principal principal) {
//        if (principal == null) {
//            throw new AccessDeniedException("Not authenticated");
//        }
//
//        // Валидация входных данных
//        if (request.getFileId() == null || request.getUserId() == null) {
//            throw new IllegalArgumentException("Invalid request data");
//        }
//
//        // Проверка прав доступа
//        if (!principal.getName().equals(request.getUserId().toString())) {
//            throw new AccessDeniedException("You can only track your own uploads");
//        }
//    }
//}