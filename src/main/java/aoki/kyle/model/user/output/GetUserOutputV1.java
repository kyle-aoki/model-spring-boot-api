package aoki.kyle.model.user.output;

import lombok.Builder;

import java.io.Serializable;
import java.util.List;

@Builder
public class GetUserOutputV1 implements Serializable {

    public enum Outcome {
        OK,
        INVALID_SESSION,
        MISSING_SESSION
    }

    public Outcome outcome;

    public String username;
    public List<String> groups;

}
