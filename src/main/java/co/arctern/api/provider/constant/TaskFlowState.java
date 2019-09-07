package co.arctern.api.provider.constant;

/**
 * Provider states for flows for particular tasks.
 */
public enum TaskFlowState {
    OPEN,
    ASSIGNED,
    ACCEPTED,
    REJECTED,
    REASSIGNED,
    OUT_FOR_PICKUP,
    OUT_FOR_DELIVERY,
    CANCELLED,
    COMPLETED,

}
