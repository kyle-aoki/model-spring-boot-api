package aoki.kyle.model.user.output;

import lombok.Builder;

@Builder
public class LogInOutputV1 {

    public enum Outcome {
        OK,
        WRONG_USERNAME_OR_PASSWORD
    }

    public Outcome outcome;

    public String sessionId;

}
