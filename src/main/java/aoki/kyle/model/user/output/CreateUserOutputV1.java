package aoki.kyle.model.user.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
public class CreateUserOutputV1 {

    // https://xyz.io/docs/CreateUserOutputV1
    public enum Outcome {
        OK,
        USERNAME_ALREADY_TAKEN,
        PASSWORD_TOO_SHORT,
        USERNAME_TOO_SHORT
    }

    public Outcome outcome;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User {
        public long id;
        public String username;
    }

    public User user;
}
