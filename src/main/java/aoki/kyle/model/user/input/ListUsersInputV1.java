package aoki.kyle.model.user.input;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListUsersInputV1 {
    public int page = 0;

    @Min(0)
    @Max(100)
    public int size = 5;
}
