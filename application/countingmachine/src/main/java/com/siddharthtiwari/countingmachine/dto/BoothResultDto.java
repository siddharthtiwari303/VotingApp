package com.siddharthtiwari.countingmachine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoothResultDto {

    private String partyName;
    private Long numberOfVotes;
}
