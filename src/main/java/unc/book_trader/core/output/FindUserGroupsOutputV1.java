package unc.book_trader.core.output;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FindUserGroupsOutputV1 {

    List<String> userGroups;

}
