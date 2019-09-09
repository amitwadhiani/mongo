package co.arctern.api.provider.dto.request;

import co.arctern.api.provider.constant.TaskType;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Task assigning request body for Admin.
 */
@Data
@NoArgsConstructor
public class TaskAssignDto {

    private Long userId;

    /**
     *
     */
    private Long refId;

    private Long orderId;
    private Boolean isPrepaid;
    private TaskType type;
    private String patientPhone;
    private String patientName;
    private String patientAge;
    private String paymentState;
    private Float amount;

    /**
     * address related detail.
     */
    private Long addressId;
    private String city;
    private String houseNumber;
    private String line;
    private String locality;
    private String pinCode;
    private String patientId;
    private String state;

}
