package com.hung.noteapp.auth.services.impls;

import com.hung.noteapp.auth.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageSource messageSource;

    @Override
    public String get(String key) {
        return messageSource.getMessage(key, null, Locale.getDefault());
    }

    @Override
    public String get(String key, Object... args) {
        return messageSource.getMessage(key, args, Locale.getDefault());
    }
}