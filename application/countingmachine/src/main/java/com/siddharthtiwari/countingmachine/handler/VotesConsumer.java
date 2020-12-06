package com.siddharthtiwari.countingmachine.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siddharthtiwari.countingmachine.dto.BoothResultDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class VotesConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    Map<String, Long> votesMap = new ConcurrentHashMap<>();

    public static final String TOPIC_NAME = "votes";

    @KafkaListener(topics = {TOPIC_NAME})
    public void handleMessage(GenericMessage message) {
        log.info("Consumed: {}", message);

        List<BoothResultDto> results = null;
        try {
            results = objectMapper.readValue((String) message.getPayload(),
                    new TypeReference<List<BoothResultDto>>(){});
        } catch (JsonProcessingException e) {
            log.error("There was an exception parsing the message.");
            log.error("The following votes weren't counted: {}", message.getPayload());
        }

        results.forEach(result -> {
                    Long currentVote = votesMap.get(result.getPartyName());
                    votesMap.put(result.getPartyName(),
                            Objects.nonNull(currentVote) ? currentVote + result.getNumberOfVotes() :
                                    result.getNumberOfVotes());
                });

    }

    public Map<String, Long>  getVotingResults(){
        return votesMap;

    }

    @Scheduled(fixedDelay = 5000)
    public void logResult() {
        log.info("In log result");
        Map<String, Long> printMap = new TreeMap<>(votesMap);
        printMap.forEach((key, value) -> {
            log.info("{} total vote {}", key, value);
        });
    }
}
