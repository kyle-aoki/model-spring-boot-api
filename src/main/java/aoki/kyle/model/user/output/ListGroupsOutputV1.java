package aoki.kyle.model.user.output;

import java.io.Serializable;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListGroupsOutputV1 implements Serializable {
    public List<String> groups;
}
