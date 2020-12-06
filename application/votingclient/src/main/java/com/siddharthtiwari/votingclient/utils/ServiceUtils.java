package com.siddharthtiwari.votingclient.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

@Slf4j
public class ServiceUtils {
    private ServiceUtils() {
    }

    public static String parseVoteString(String line) {
        try {
            return line.split(";")[2];
        } catch(Exception e) {
            log.error("Invalid input format: {}", line);
            throw new RuntimeException("Invalid input format!");
        }
    }
}
