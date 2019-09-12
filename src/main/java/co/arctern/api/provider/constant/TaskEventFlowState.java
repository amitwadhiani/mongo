package co.arctern.api.provider.constant;

/**
 * task states for flows for particular tasks.
 */
public enum TaskEventFlowState {
    OPEN,
    ASSIGNED,
    ACCEPTED,
    RESCHEDULED,
    REJECTED,
    REASSIGNED,
    STARTED,
    OUT_FOR_PICKUP,
    OUT_FOR_DELIVERY,
    CANCELLED,
    COMPLETED,

}
