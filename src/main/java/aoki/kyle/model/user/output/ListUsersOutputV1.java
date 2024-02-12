package aoki.kyle.model.user.output;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListUsersOutputV1 {

    @Data
    public static class User {
        public long id;
        public String username;
    }

    public List<User> users;
}
