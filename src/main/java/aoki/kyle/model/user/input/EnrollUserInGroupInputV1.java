package aoki.kyle.model.user.input;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EnrollUserInGroupInputV1 {

    public String username;
    public String groupname;

}
