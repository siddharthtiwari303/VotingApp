import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;

public class VoteGenerator {

    private static final int NUM_OF_PARTIES = 20;
    private static final String BOOTH_NAME = "booth1";

    public static void main(String[] args) throws InterruptedException, IOException {

        Path path = Paths.get("../votes/" + BOOTH_NAME + ".log");
        if(!Files.exists(path)) {
            Files.createFile(path);
        }

        long voterId = 1;
        Random generator = new Random();

        while(true) {
            Thread.sleep(3000);
            String vote = new StringBuilder().append("voter")
                    .append(voterId)
                    .append(";")
                    .append(BOOTH_NAME)
                    .append(";")
                    .append("party")
                    .append(generator.nextInt(NUM_OF_PARTIES) + 1)
                    .append("\n")
                    .toString();

            System.out.println(vote);
            byte[] strToBytes = vote.getBytes();
            Files.write(path, strToBytes, StandardOpenOption.APPEND);
            voterId++;
        }
    }
}
