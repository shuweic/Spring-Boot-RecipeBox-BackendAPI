package com.springboot.recipebox.controller;

import com.springboot.recipebox.entity.Message;
import com.springboot.recipebox.repository.MessageRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@Controller      // this is needed for this to be an endpoint to springboot
@ServerEndpoint(value = "/chat/{username}")  // this is Websocket url
public class ChatSocket {

    // cannot autowire static directly (instead we do it by the below
    // method
    private static MessageRepository msgRepo;

    /*
     * Grabs the MessageRepository singleton from the Spring Application
     * Context.  This works because of the @Controller annotation on this
     * class and because the variable is declared as static.
     * There are other ways to set this. However, this approach is
     * easiest...
     */
    @Autowired
    public void setMessageRepository(MessageRepository repo)
    {
        msgRepo = repo;  // we are setting the static variable
    }

    // Store all socket session and their corresponding username.
    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(ChatSocket.class);

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username)
    {
        logger.info("Entered into Open");

        // store connecting user information
        sessionUsernameMap.put(session, username);
        usernameSessionMap.put(username, session);

        //Send chat history to the newly connected user
        sendMessageToParticularUser(username, getChatHistory());

        // broadcast that new user joined
        String message = "User: " + username + " is online";
        broadcast(message);
    }


    @OnMessage
    public void onMessage(Session session, String message)
    {
        // Handle new messages
        logger.info("Entered into Message: Got Message:" + message);
        String username = sessionUsernameMap.get(session);

        // Direct message to a user using the format "@username <message>"
        if (message.startsWith("@")) {
            String destUsername = message.split(" ")[0].substring(1);

            // send the message to the sender and receiver
            sendMessageToParticularUser(destUsername, "[DM] " + username + ": " + message);
            sendMessageToParticularUser(username, "[DM] " + username + ": " + message);

        }
        else { // broadcast
            broadcast(username + ": " + message);
        }

        // Saving chat history to repository
        msgRepo.save(new Message(username, message));
    }


    @OnClose
    public void onClose(Session session)
    {
        logger.info("Entered into Close");

        // remove the user connection information
        String username = sessionUsernameMap.get(session);
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);

        // broadcase that the user disconnected
        String message = username + " disconnected";
        broadcast(message);
    }


    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        logger.info("Entered into Error");
        throwable.printStackTrace();
    }


    private void sendMessageToParticularUser(String username, String message)
    {
        try
        {
            usernameSessionMap.get(username).getBasicRemote().sendText(message);
        }
        catch (IOException e)
        {
            logger.info("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void broadcast(String message)
    {
        sessionUsernameMap.forEach((session, username) ->
        {
            try
            {
                session.getBasicRemote().sendText(message);
            }
            catch (IOException e)
            {
                logger.info("Exception: " + e.getMessage());
                e.printStackTrace();
            }

        });

    }


    // Gets the Chat history from the repository
    private String getChatHistory()
    {
        List<Message> messages = msgRepo.findAll();

        // convert the list to a string
        StringBuilder sb = new StringBuilder();
        if(messages != null && messages.size() != 0)
        {
            for (Message message : messages) {
                sb.append(message.getUserName() + ": " + message.getContent() + "\n");
            }
        }
        return sb.toString();
    }

} // end of Class