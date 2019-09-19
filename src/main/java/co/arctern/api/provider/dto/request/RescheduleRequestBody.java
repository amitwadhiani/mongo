package co.arctern.api.provider.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class RescheduleRequestBody {

    private Long taskId;
    private Long userId;
    private Timestamp time;
}
