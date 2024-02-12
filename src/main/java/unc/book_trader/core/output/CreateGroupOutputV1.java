package unc.book_trader.core.output;

import lombok.Builder;

@Builder
public class CreateGroupOutputV1 {

    public enum Outcome {
        OK,
        GROUP_ALREADY_EXISTS
    }

    public Outcome outcome;
}
