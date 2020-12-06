package com.siddharthtiwari.votingclient.service;

import com.siddharthtiwari.votingclient.dto.BoothResultDto;
import com.siddharthtiwari.votingclient.utils.JsonUtils;
import com.siddharthtiwari.votingclient.utils.ServiceUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;


@Component
@Slf4j
public class VotingService implements CommandLineRunner {

    private TailerListener listener;

    @Value("file:../votes/${booth.name}.log")
    private Resource file;

    public VotingService(TailerListener listener) {
        this.listener = listener;
    }

    @Override
    public void run(String... args) throws Exception {
        Tailer tailer = new Tailer(file.getFile(), listener, 500);
        Executor executor = Runnable::run;
        executor.execute(tailer);
    }

    @Component
    static class VotingTailerListener extends TailerListenerAdapter {

        public static final int THRESHOLD = 2;
        private static final Map<String, Long> votes = new HashMap<>();
        public static final String STOP_VOTE_COUNT = "***";
        private static int count = 0;

        @Autowired
        private KafkaTemplate<String, String> template;

        @Autowired
        private JsonUtils jsonUtils;

        private Tailer tailer;

        public void init(Tailer tailer) {
            this.tailer = tailer;
        }

        @Override
        public void handle(Exception ex) {
            log.info("There was an exception: ", ex);
        }

        @Override
        public void handle(String line) {
            if(StringUtils.isEmpty(line)) {
                return;
            }
            log.info("Processing Lines:{}", line);
            if(!STOP_VOTE_COUNT.equals(line)) {
                String party = ServiceUtils.parseVoteString(line);
                Long currentVotes = votes.get(party);
                votes.put(party, Objects.isNull(currentVotes) ? 1L : currentVotes + 1);
                count++;
                if(count >= THRESHOLD) {
                    sendMessage();
                }
            } else {
                if(!CollectionUtils.isEmpty(votes)) {
                    sendMessage();
                }
                tailer.stop();
                log.info("Tailer exited! All votes counted.");
            }
        }

        private void sendMessage() {
            List<BoothResultDto> output = votes.entrySet().stream()
                    .map(entry -> new BoothResultDto(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());

            log.info("Publishing votes: {}" , output);

            template.send("votes", jsonUtils.covertDto(output));
            votes.clear();
            count = 0;
        }

    }
}
