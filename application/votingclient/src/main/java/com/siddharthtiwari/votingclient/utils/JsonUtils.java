package com.siddharthtiwari.votingclient.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class JsonUtils {

    private ObjectMapper objectMapper;

    public String covertDto(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Error converting the object: {}", object);
            return StringUtils.EMPTY;
        }
    }

}
