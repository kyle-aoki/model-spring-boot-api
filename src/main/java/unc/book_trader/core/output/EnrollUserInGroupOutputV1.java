package unc.book_trader.core.output;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnrollUserInGroupOutputV1 {

    public enum Outcome {
        OK,
        USER_ALREADY_IN_GROUP,
        FAILURE
    }

    public Outcome outcome;

}
