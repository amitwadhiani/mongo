package co.arctern.api.provider.constant;

/**
 * task states for flows for particular tasks.
 */
public enum TaskStateFlowState {

    OPEN,
    ASSIGNED,
    AUTO_ASSIGNED,
    AUTO_STARTED,
    ACCEPTED,
    RESCHEDULED,
    REJECTED,
    TIMED_OUT,
    REASSIGNED,
    STARTED,
    OUT_FOR_PICKUP,
    OUT_FOR_DELIVERY,
    CANCELLED,
    CANCELLATION_REQUESTED,
    COMPLETED
}
