package com.hung.noteapp.auth.services;

public interface MessageService {
    String get(String key);
    String get(String key, Object... args);
}
