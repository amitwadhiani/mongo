package co.arctern.api.provider.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Task assigning request body for Admin.
 */
@Data
@NoArgsConstructor
public class TaskAssignDto {

    private Long userId;
    private Long diagnosticOrderId;
    private String paymentState;
    private Float amount;
}
