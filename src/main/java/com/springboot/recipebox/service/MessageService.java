package com.springboot.recipebox.service;

import com.springboot.recipebox.payload.MessageDTO;

import java.util.List;

public interface MessageService {
    MessageDTO sendMessage(MessageDTO messageDto);

    List<MessageDTO> getAllMessage();

    MessageDTO getMessageById(int message_id);

    void deletePostById(int message_id);
}
