package aoki.kyle.model.user.output;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FindUserGroupsOutputV1 {

    List<String> userGroups;

}
