package unc.book_trader.core.input;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EnrollUserInGroupInputV1 {

    public String username;
    public String groupname;

}
