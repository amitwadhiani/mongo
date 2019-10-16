package co.arctern.api.provider.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReasonEditBody {

    private Long reasonId;
    private Boolean isActive;
}
