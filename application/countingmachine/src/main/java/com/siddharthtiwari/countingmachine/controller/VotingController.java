package com.siddharthtiwari.countingmachine.controller;

import com.siddharthtiwari.countingmachine.handler.VotesConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class VotingController {

    @Autowired
    VotesConsumer votesService;

    @GetMapping(value = "/getVotingResults")
    public Map<String, Long> getVotingResults() {
        return  votesService.getVotingResults();
    }
}
