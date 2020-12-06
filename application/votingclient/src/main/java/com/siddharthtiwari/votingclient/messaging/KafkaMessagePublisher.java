package com.siddharthtiwari.votingclient.messaging;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@AllArgsConstructor
public class KafkaMessagePublisher<T> {

    private final KafkaTemplate<String, T> template;
    private final String TOPIC_NAME;

    public void send(T event) {
        template.send(TOPIC_NAME, event);
    }
}
