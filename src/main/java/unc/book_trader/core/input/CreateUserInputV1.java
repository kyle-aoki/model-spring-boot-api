package unc.book_trader.core.input;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
public class CreateUserInputV1 {
    @Length(max = 64)
    public String username;

    @Length(max = 128)
    public String password;
}
