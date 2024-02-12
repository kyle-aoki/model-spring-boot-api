package aoki.kyle.model.user.input;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogInInputV1 {

    public String username;
    public String password;

    public boolean sendSessionInResponseBody;

}
