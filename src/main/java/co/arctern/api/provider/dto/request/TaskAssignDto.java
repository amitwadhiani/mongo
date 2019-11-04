package co.arctern.api.provider.dto.request;

import co.arctern.api.provider.constant.PaymentState;
import co.arctern.api.provider.constant.TaskType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * Task assigning request body for Admin.
 */
@Data
@NoArgsConstructor
public class TaskAssignDto {

    private Long userId;

    /**
     * orderId from order-api.
     */
    private Long refId;
    private Long taskId;

    private Boolean isPrepaid;
    private TaskType type;
    private String source;
    private Long diagnosticOrderId;
    private String landmark;
    private String patientPhone;
    private String patientName;
    private String patientAge;
    private PaymentState paymentState;
    private String paymentMode;
    private Double amount;
    private Timestamp expectedArrivalTime;

    /**
     * address related detail.
     */
    private String addressName;
    private Long destAddressId;
    private Double latitude;
    private Boolean isSourceAddress;
    private Long areaId;
    private Double longitude;
    private Long sourceAddressId;
    private String city;
    private String houseNumber;
    private String line;
    private String locality;
    private String pinCode;
    private String patientId;
    private String state;

}
