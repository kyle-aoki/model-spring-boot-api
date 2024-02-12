package aoki.kyle.model.user.input;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateGroupInputV1 {

    public String groupname;
}
