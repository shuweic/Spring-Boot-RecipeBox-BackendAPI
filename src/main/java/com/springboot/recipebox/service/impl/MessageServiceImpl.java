package com.springboot.recipebox.service.impl;

import com.springboot.recipebox.entity.Message;
import com.springboot.recipebox.exception.ResourceNotFoundException;
import com.springboot.recipebox.payload.MessageDTO;
import com.springboot.recipebox.repository.MessageRepository;
import com.springboot.recipebox.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    private MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public MessageDTO sendMessage(MessageDTO messageDto) {
//        Message message = mapToEntity(messageDto);
//        Message newMessage = messageRepository.save(message);
//
//        MessageDTO messageResponse = mapToDTO(newMessage);
//        return messageResponse;
        return null;
    }

    @Override
    public List<MessageDTO> getAllMessage() {
//        List<Message> messages = messageRepository.findAll();
//        return messages.stream().map(message -> mapToDTO(message)).collect(Collectors.toList());
        return null;
    }

    @Override
    public MessageDTO getMessageById(int message_id) {
//        Message message = messageRepository.findById(message_id).orElseThrow(() -> new ResourceNotFoundException("Message not found", "message_id", message_id));
//        return mapToDTO(message);
        return null;
    }

    @Override
    public void deletePostById(int message_id) {
//        Message message = messageRepository.findById(message_id).orElseThrow(() -> new ResourceNotFoundException("Message not found", "message_id", message_id));
//        messageRepository.delete(message);
    }


//    // Entity to DTO
//    private MessageDTO mapToDTO(Message message) {
//        MessageDTO messageDto = new MessageDTO();
//        messageDto.setMessage_id(message.getMessage_id());
//        messageDto.setContent(message.getContent());
//        messageDto.setDate_sent(message.getDate_sent());
//        return messageDto;
//    }
//    // DTO to Entity
//
//    private Message mapToEntity(MessageDTO messageDto) {
//        Message message = new Message();
//        message.setContent(messageDto.getContent());
//        message.setSent(messageDto.getDate_sent());
//        return message;
//    }
}
