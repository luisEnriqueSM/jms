package org.beer.works.jms.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import lombok.RequiredArgsConstructor;
import org.beer.works.jms.config.JmsConfig;
import org.beer.works.jms.model.HelloWorldMessage;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class HelloSender {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 2000)
    public void sendMessage(){
        //System.out.println("I'm sending a message");
        HelloWorldMessage message = HelloWorldMessage
                .builder()
                .id(UUID.randomUUID())
                .message("Hello world!")
                .build();

        jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, message);

        //System.out.println("Message Sent!");
    }

    @Scheduled(fixedRate = 2000)
    public void receiveMessage() throws JMSException {
        HelloWorldMessage message = HelloWorldMessage
                .builder()
                .id(UUID.randomUUID())
                .message("Hello")
                .build();

        Message receivedMsg = jmsTemplate.sendAndReceive(JmsConfig.MY_SEND_RCV_QUEUE, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                Message helloMessage = null;
                try {
                    helloMessage = session.createTextMessage(objectMapper.writeValueAsString(message));
                    helloMessage.setStringProperty("_type",  "org.beer.works.jms.model.HelloWorldMessage");

                    System.out.println("Sending Hello");

                    return helloMessage;
                } catch (JsonProcessingException e) {
                    throw new JMSException("boom");
                }
            }
        });

        System.out.println(receivedMsg.getBody(String.class));
    }
}
